//package br.com.tick.tickdesck.config.llm;
//
//import br.com.tick.tickdesck.config.email.dto.EmailCallDataDto;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ClaudeAiService {
//    private static final Logger log = LoggerFactory.getLogger(ClaudeAiService.class);
//
//    @Value("${claude.api.key}")
//    private String apiKey;
//
//    @Value("${claude.api.url:https://api.anthropic.com/v1/messages}")
//    private String apiUrl;
//
//    @Value("${claude.model:claude-sonnet-4-5-20250929}")
//    private String model;
//
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    public ClaudeAiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
//        this.restTemplate = restTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    //      Extrai dados estruturados de um email usando Claude AI
//
//    public EmailCallDataDto extractCallDataFromEmail(String emailFrom, String emailSubject, String emailBody) {
//        try {
//            String prompt = buildPrompt(emailFrom, emailSubject, emailBody);
//            String response = callClaudeApi(prompt);
//            return parseClaudeResponse(response);
//        } catch (Exception e) {
////            log.error("Erro ao extrair dados do email com Claude AI", e);
//            throw new RuntimeException("Falha ao processar email com IA", e);
//        }
//    }
//
//    private String buildPrompt(String from, String subject, String body) {
//        return String.format("""
//            Você é um assistente especializado em extrair informações estruturadas de emails para criar chamados de help desk.
//
//            Analise o email abaixo e extraia as seguintes informações em formato JSON:
//
//            Email:
//            De: %s
//            Assunto: %s
//            Corpo: %s
//
//            Retorne APENAS um JSON válido (sem markdown, sem explicações) com esta estrutura exata:
//            {
//              "title": "título do chamado (máximo 100 caracteres, resumo claro do problema)",
//              "urgency": "BAIXA ou MEDIA ou ALTA (baseado na urgência implícita ou explícita no email)",
//              "description": "descrição detalhada do problema extraída do corpo do email",
//              "requisitanteEmail": "email do remetente",
//              "requisitanteName": "nome do remetente se identificável, ou vazio"
//            }
//
//            Regras para classificação de urgência:
//            - ALTA: sistema fora do ar, erro crítico, bloqueio total, palavras como "urgente", "crítico", "parado"
//            - MEDIA: problema que afeta o trabalho mas tem workaround, pedidos normais
//            - BAIXA: dúvidas, sugestões, melhorias, não é urgente
//
//            Se não conseguir identificar alguma informação, use valores vazios ("") mas mantenha a estrutura JSON.
//            """, from, subject, body);
//    }
//
//    private String callClaudeApi(String prompt) throws Exception {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("x-api-key", apiKey);
//        headers.set("anthropic-version", "2023-06-01");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", model);
//        requestBody.put("max_tokens", 1024);
//        requestBody.put("messages", List.of(
//                Map.of(
//                        "role", "user",
//                        "content", prompt
//                )
//        ));
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//        log.debug("Enviando requisição para Claude API...");
//        String response = restTemplate.postForObject(apiUrl, request, String.class);
//        log.debug("Resposta recebida da Claude API");
//
//        return response;
//    }
//
//    private EmailCallDataDto parseClaudeResponse(String response) throws Exception {
//        JsonNode root = objectMapper.readTree(response);
//
//        // A resposta da API Claude tem formato: { "content": [{ "text": "..." }] }
//        String contentText = root.path("content").get(0).path("text").asText();
//
//        log.debug("Conteúdo extraído: {}", contentText);
//
//        // Remove possíveis markdown code blocks se existirem
//        contentText = contentText.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
//
//        // Parse do JSON extraído
//        JsonNode dataNode = objectMapper.readTree(contentText);
//
//        return EmailCallDataDto.builder()
//                .title(dataNode.path("title").asText())
//                .urgency(dataNode.path("urgency").asText())
//                .description(dataNode.path("description").asText())
//                .requisitanteEmail(dataNode.path("requisitanteEmail").asText())
//                .requisitanteName(dataNode.path("requisitanteName").asText())
//                .build();
//    }
//
//}
