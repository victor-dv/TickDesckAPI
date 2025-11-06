package br.com.tick.tickdesck.config.email.application;

import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.scheduling.PollerMetadata;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.time.Duration;
import java.util.Properties;

@Configuration
public class EmailIntegrationConfig {

    private static final Logger log = LoggerFactory.getLogger(EmailIntegrationConfig.class);

    @Value("${mail.imap.host}")
    private String imapHost;

    @Value("${mail.imap.port}")
    private int imapPort;

    @Value("${mail.imap.user}")
    private String imapUser;

    @Value("${mail.imap.password}")
    private String imapPassword;

    @Value("${email.call.auto.create:true}")
    private boolean autoCreateCall;

    @Autowired
    private EmailParsingService emailParsingService;

    @Autowired
    private EmailCallProcessorService emailCallProcessorService;

    @Bean
    public ImapMailReceiver imapMailReceiver() {
        try {
            String encodedUser = URLEncoder.encode(imapUser, StandardCharsets.UTF_8);
            String encodedPass = URLEncoder.encode(imapPassword, StandardCharsets.UTF_8);

            String url = String.format("imaps://%s:%s@%s:%d/INBOX",
                    encodedUser, encodedPass, imapHost, imapPort);

            ImapMailReceiver receiver = new ImapMailReceiver(url);
            receiver.setShouldDeleteMessages(false);
            receiver.setShouldMarkMessagesAsRead(true);
            receiver.setAutoCloseFolder(false);

            Properties javaMailProperties = new Properties();
            javaMailProperties.put("mail.imaps.ssl.enable", "true");
            javaMailProperties.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            javaMailProperties.put("mail.imaps.socketFactory.fallback", "false");
            javaMailProperties.put("mail.imaps.socketFactory.port", imapPort);
            javaMailProperties.put("mail.imaps.connectiontimeout", "5000");
            javaMailProperties.put("mail.imaps.timeout", "5000");
            javaMailProperties.put("mail.mime.allowencodedmessages", "true");
            javaMailProperties.put("mail.mime.decodefilename", "true");

            receiver.setJavaMailProperties(javaMailProperties);

            log.info("IMAP configurado | Host: {} | User: {}", imapHost, imapUser);
            return receiver;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao configurar a URL do IMAPReceiver", e);
        }
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec defaultPoller() {
        return Pollers.fixedDelay(Duration.ofSeconds(30))
                .maxMessagesPerPoll(10);
    }

    @Bean
    public IntegrationFlow emailInboundFlow(ImapMailReceiver imapMailReceiver) {
        return IntegrationFlow.from(Mail.imapInboundAdapter(imapMailReceiver))
                .channel("emailInboundChannel")
                .handle(MimeMessage.class, (payload, headers) -> {
                    try {
                        log.info("📬 NOVO E-MAIL RECEBIDO | Assunto: {}", payload.getSubject());

                        if (autoCreateCall) {
                            CallsEntity call = emailCallProcessorService.processEmailAndCreateCall(payload);

                            log.info("🎫 Chamado #{} criado automaticamente a partir do email",
                                    call.getNumberCall());

                        } else {
                            String from = (payload.getFrom() != null && payload.getFrom().length > 0)
                                    ? payload.getFrom()[0].toString()
                                    : "desconhecido";
                            String subject = payload.getSubject();
                            String body = emailParsingService.parseEmailContent(payload);

                            log.info("📨 Email processado (auto-create desabilitado) | De: {} | Assunto: {}",
                                    from, subject);
                            log.debug("Conteúdo: {}...",
                                    body.substring(0, Math.min(body.length(), 100)));
                        }

                    } catch (Exception e) {
                        log.error("❌ Erro ao processar e-mail", e);
                    }
                    return null;
                })
                .get();
    }
}