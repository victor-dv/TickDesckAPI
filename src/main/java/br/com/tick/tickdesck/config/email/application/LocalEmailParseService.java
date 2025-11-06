package br.com.tick.tickdesck.config.email.application;

import br.com.tick.tickdesck.config.email.dto.EmailCallDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LocalEmailParseService {
    private static final Logger log = LoggerFactory.getLogger(LocalEmailParseService.class);


    public EmailCallDataDto extractCallDataFromEmail(String emailFrom, String emailSubject, String emailBody) {
        log.info("🔧 Usando parser local (sem IA) para processar email");

        String title = extractTitle(emailSubject, emailBody);
        String urgency = classifyUrgency(emailSubject, emailBody);
        String description = extractDescription(emailBody);
        String requisitanteEmail = emailFrom;
        String requisitanteName = extractNameFromEmail(emailFrom);

        return EmailCallDataDto.builder()
                .title(title)
                .urgency(urgency)
                .description(description)
                .requisitanteEmail(requisitanteEmail)
                .requisitanteName(requisitanteName)
                .build();
    }


    private String extractTitle(String subject, String body) {
        if (subject != null && !subject.trim().isEmpty()) {
            String cleanSubject = subject
                    .replaceAll("(?i)^(re:|fwd:|fw:|enc:)\\s*", "")
                    .trim();

            if (cleanSubject.length() > 100) {
                return cleanSubject.substring(0, 97) + "...";
            }
            return cleanSubject;
        }

        // Se não tiver assunto, usa as primeiras linhas do corpo
        if (body != null && !body.trim().isEmpty()) {
            String firstLine = body.split("\\n")[0].trim();
            if (firstLine.length() > 100) {
                return firstLine.substring(0, 97) + "...";
            }
            return firstLine;
        }

        return "Chamado via Email";
    }


     // Classifica a urgência baseado em palavras-chave

    private String classifyUrgency(String subject, String body) {
        String content = (subject + " " + body).toLowerCase();

        // Palavras-chave para ALTA urgência
        String[] highUrgencyKeywords = {
                "urgente", "crítico", "emergência", "parado", "fora do ar",
                "não funciona", "travado", "bloqueado", "imediato", "asap",
                "prioridade alta", "sistema down", "erro crítico", "problema grave"
        };

        // Palavras-chave para BAIXA urgência
        String[] lowUrgencyKeywords = {
                "dúvida", "sugestão", "melhoria", "quando possível", "futuro",
                "ideia", "pergunta", "consulta", "informação", "como faço"
        };

        // Verifica urgência ALTA
        for (String keyword : highUrgencyKeywords) {
            if (content.contains(keyword)) {
                log.debug("Classificado como ALTA urgência (palavra-chave: {})", keyword);
                return "ALTA";
            }
        }

        // Verifica urgência BAIXA
        for (String keyword : lowUrgencyKeywords) {
            if (content.contains(keyword)) {
                log.debug("Classificado como BAIXA urgência (palavra-chave: {})", keyword);
                return "BAIXA";
            }
        }

        // Padrão: MÉDIA
        log.debug("Classificado como MEDIA urgência (padrão)");
        return "MEDIA";
    }


     // Extrai descrição formatada do corpo do email

    private String extractDescription(String body) {
        if (body == null || body.trim().isEmpty()) {
            return "Sem descrição fornecida.";
        }

        // Remove assinaturas comuns de email
        String cleanBody = body
                .replaceAll("(?i)\\n--\\s*\\n.*", "") // Remove assinatura após --
                .replaceAll("(?i)enviado do meu .*", "") // Remove "Enviado do meu iPhone"
                .replaceAll("(?i)sent from my .*", "")
                .trim();

        // Limita o tamanho se necessário (você pode ajustar)
        if (cleanBody.length() > 1000) {
            return cleanBody.substring(0, 997) + "...";
        }

        return cleanBody;
    }


    private String extractNameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }

        String localPart = email.split("@")[0];

        String name = localPart
                .replaceAll("[._-]", " ")
                .replaceAll("\\d+", "")
                .trim();

        if (!name.isEmpty()) {
            String[] words = name.split("\\s+");
            StringBuilder formattedName = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    formattedName.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
                }
            }
            return formattedName.toString().trim();
        }

        return localPart;
    }
}
