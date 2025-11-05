package br.com.tick.tickdesck.config;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeMessage;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailParsingService {
    private static final Logger log = LoggerFactory.getLogger(EmailParsingService.class);
    
    public String parseEmailContent(MimeMessage message) {
        try {
            return getTextFromMessage(message);
        } catch (Exception e) {
            log.error("Erro ao fazer parsing do conteúdo do e-mail", e);
            return "Erro ao ler o corpo do e-mail.";
        }
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            String html = message.getContent().toString();
            result = Jsoup.parse(html).text();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            result = getTextFromMultipart(multipart);
        }
        return result;
    }

    private String getTextFromMultipart(Multipart multipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = multipart.getCount();
        log.debug("Este e-mail é 'multipart' com {} partes.", count);

        String textContent = null;
        String htmlContent = null;

        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);

            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                log.debug("Ignorando parte de anexo: {}", bodyPart.getFileName());
                continue;
            }

            if (bodyPart.isMimeType("text/plain")) {
                textContent = bodyPart.getContent().toString();
                log.debug("Encontrado text/plain.");
            } else if (bodyPart.isMimeType("text/html")) {
                htmlContent = bodyPart.getContent().toString();
                log.debug("Encontrado text/html.");
            } else if (bodyPart.isMimeType("multipart/alternative")) {
                log.debug("Encontrada parte 'multipart/alternative', analisando recursivamente...");
                return getTextFromMultipart((Multipart) bodyPart.getContent());
            } else if (bodyPart.isMimeType("multipart/*")) {
                result.append(getTextFromMultipart((Multipart) bodyPart.getContent()));
            }
        }


        if (textContent != null) {
            log.debug("Retornando conteúdo text/plain.");
            result.append(textContent);
        } else if (htmlContent != null) {
            log.debug("Retornando conteúdo text/html convertido para texto.");
            result.append(Jsoup.parse(htmlContent).text());
        }

        return result.toString();
    }
}
