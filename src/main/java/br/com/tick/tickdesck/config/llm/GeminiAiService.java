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

import java.util.ArrayList;
import java.util.List;

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
            Você é um assistente especializado em análise de emails para criação de chamados de help desk.
            Sua função é extrair informações estruturadas, limpar o conteúdo de palavrões e linguagem inadequada, e sugerir soluções práticas.
            
            Email a analisar:
            De: %s
            Assunto: %s
            Corpo: %s
            
            INSTRUÇÕES IMPORTANTES:
            
            1. LIMPEZA DE CONTEÚDO:
               - REMOVA TODOS os palavrões, xingamentos, palavras ofensivas e linguagem inadequada do texto
               - Substitua por termos neutros e profissionais (ex: "problema", "dificuldade", "erro", "falha")
               - Mantenha o significado técnico e a informação relevante
               - Preserve a urgência e o contexto do problema mesmo após a limpeza
            
            2. ESTRUTURA DE RESPOSTA:
            Retorne APENAS um JSON válido (sem markdown, sem explicações adicionais) com esta estrutura exata:
            {
              "title": "título claro e objetivo baseado no assunto (máximo 100 caracteres, remova prefixos RE:, FWD:, etc)",
              "urgency": "BAIXA ou MEDIA ou ALTA",
              "description": "descrição detalhada e profissional do problema, SEM palavrões ou linguagem inadequada. Inclua: sintomas observados, mensagens de erro (se houver), passos para reproduzir o problema, contexto do usuário, impacto no trabalho",
              "requisitanteEmail": "email do remetente",
              "requisitanteName": "nome do remetente se identificável no email, caso contrário deixe vazio",
              "suggestedSolutions": ["solução 1", "solução 2", "solução 3"]
            }
            
            3. REGRAS PARA CLASSIFICAÇÃO DE URGÊNCIA:
               - ALTA: sistema completamente fora do ar, erro crítico que impede trabalho, bloqueio total de acesso, perda de dados, palavras como "urgente", "crítico", "parado", "não consigo trabalhar"
               - MEDIA: problema que afeta o trabalho mas possui alternativas/workarounds, lentidão significativa, funcionalidades parciais
               - BAIXA: dúvidas, solicitações de informação, melhorias sugeridas, problemas menores que não impedem o trabalho
            
            4. DESCRIÇÃO DO PROBLEMA:
               - Seja detalhado e específico
               - Inclua informações técnicas relevantes (mensagens de erro, códigos, URLs)
               - Descreva o comportamento esperado vs comportamento atual
               - Mencione tentativas de solução já realizadas (se houver no email)
               - Use linguagem profissional e clara
               - SEMPRE remova palavrões, xingamentos e linguagem ofensiva
            
            5. SUGESTÕES DE SOLUÇÕES:
               - Gere 2 a 4 soluções práticas e viáveis baseadas no problema descrito
               - Soluções devem ser específicas e acionáveis
               - Ordene por probabilidade de sucesso (mais provável primeiro)
               - Inclua soluções como: verificar configurações, reiniciar serviços, atualizar software, verificar logs, contatar suporte específico, etc.
               - Se o problema não for claro, sugira investigações iniciais
            
            6. TRATAMENTO DO ASSUNTO:
               - Use o ASSUNTO do email como base para o título
               - Remova prefixos como RE:, FWD:, FW:, ENC:, [URGENTE], etc.
               - Se o assunto estiver vazio ou muito genérico, use as primeiras palavras relevantes do corpo
               - Título deve ser claro e descritivo do problema
            
            EXEMPLO DE LIMPEZA:
            Antes: "Essa merda não funciona de jeito nenhum, já tentei de tudo!"
            Depois: "O sistema apresenta falhas constantes após múltiplas tentativas de utilização."
            
            IMPORTANTE: Se não conseguir identificar alguma informação, use valores vazios ("") ou arrays vazios ([]) mas SEMPRE mantenha a estrutura JSON válida.
            NUNCA inclua palavrões ou linguagem inadequada na resposta.
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

        // Extrai as soluções sugeridas
        List<String> suggestedSolutions = new ArrayList<>();
        JsonNode solutionsNode = dataNode.path("suggestedSolutions");
        if (solutionsNode.isArray()) {
            for (JsonNode solutionNode : solutionsNode) {
                String solution = solutionNode.asText();
                if (solution != null && !solution.trim().isEmpty()) {
                    suggestedSolutions.add(solution.trim());
                }
            }
        }

        return EmailCallDataDto.builder()
                .title(dataNode.path("title").asText())
                .urgency(dataNode.path("urgency").asText())
                .description(dataNode.path("description").asText())
                .requisitanteEmail(dataNode.path("requisitanteEmail").asText())
                .requisitanteName(dataNode.path("requisitanteName").asText())
                .suggestedSolutions(suggestedSolutions.isEmpty() ? null : suggestedSolutions)
                .build();
    }
}


