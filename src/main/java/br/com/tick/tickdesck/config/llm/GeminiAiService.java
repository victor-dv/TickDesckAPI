package br.com.tick.tickdesck.config.llm;

import br.com.tick.tickdesck.config.email.dto.EmailCallDataDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiAiService {
    private static final Logger log = LoggerFactory.getLogger(GeminiAiService.class);

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    private final Client client;
    private final ObjectMapper objectMapper;

    @Autowired
    public GeminiAiService(ObjectMapper objectMapper) {
        // O Client obtém a API key da variável de ambiente GEMINI_API_KEY
        this.client = new Client();
        this.objectMapper = objectMapper;
    }

    /**
     * Extrai dados estruturados de um email usando Gemini AI
     */
    public EmailCallDataDto extractCallDataFromEmail(String emailFrom, String emailSubject, String emailBody) {
        try {
            String prompt = buildPrompt(emailFrom, emailSubject, emailBody);
            String response = callGeminiApi(prompt);
            return parseGeminiResponse(response);
        } catch (Exception e) {
            log.error("Erro ao extrair dados do email com Gemini AI", e);
            throw new RuntimeException("Falha ao processar email com IA", e);
        }
    }

    private String buildPrompt(String from, String subject, String body) {
        return String.format("""
            Você é um assistente especializado em extrair informações estruturadas de emails para criar chamados de help desk.
            
            Analise o email abaixo e extraia as seguintes informações em formato JSON:
            
            Email:
            De: %s
            Assunto: %s
            Corpo: %s
            
            Retorne APENAS um JSON válido (sem markdown, sem explicações) com esta estrutura exata:
            {
              "title": "use o ASSUNTO do email como título (máximo 100 caracteres, remova prefixos como RE:, FWD:, etc)",
              "urgency": "BAIXA ou MEDIA ou ALTA (baseado na urgência implícita ou explícita no email)",
              "description": "descrição detalhada do problema extraída do corpo do email",
              "requisitanteEmail": "email do remetente",
              "requisitanteName": "nome do remetente se identificável, ou vazio"
            }
            
            IMPORTANTE: O campo "title" deve ser o ASSUNTO do email (após remover prefixos como RE:, FWD:, etc). 
            Se o assunto estiver vazio, use as primeiras palavras do corpo do email.
            
            Regras para classificação de urgência:
            - ALTA: sistema fora do ar, erro crítico, bloqueio total, palavras como "urgente", "crítico", "parado"
            - MEDIA: problema que afeta o trabalho mas tem workaround, pedidos normais
            - BAIXA: dúvidas, sugestões, melhorias, não é urgente
            
            Se não conseguir identificar alguma informação, use valores vazios ("") mas mantenha a estrutura JSON.
            """, from, subject, body);
    }

    private String callGeminiApi(String prompt) throws Exception {
        log.debug("Enviando requisição para Gemini API...");

        GenerateContentResponse response = client.models.generateContent(
                model,
                prompt,
                null);

        log.debug("Resposta recebida da Gemini API");

        return response.text();
    }

    private EmailCallDataDto parseGeminiResponse(String response) throws Exception {
        log.debug("Conteúdo extraído: {}", response);

        // Remove possíveis markdown code blocks se existirem
        String contentText = response.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();

        // Tenta extrair JSON se estiver dentro de texto
        int jsonStart = contentText.indexOf("{");
        int jsonEnd = contentText.lastIndexOf("}");

        if (jsonStart >= 0 && jsonEnd > jsonStart) {
            contentText = contentText.substring(jsonStart, jsonEnd + 1);
        }

        // Parse do JSON extraído
        JsonNode dataNode = objectMapper.readTree(contentText);

        return EmailCallDataDto.builder()
                .title(dataNode.path("title").asText())
                .urgency(dataNode.path("urgency").asText())
                .description(dataNode.path("description").asText())
                .requisitanteEmail(dataNode.path("requisitanteEmail").asText())
                .requisitanteName(dataNode.path("requisitanteName").asText())
                .build();
    }
}


