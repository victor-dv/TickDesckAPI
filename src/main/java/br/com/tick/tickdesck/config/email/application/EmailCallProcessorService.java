package br.com.tick.tickdesck.config.email.application;

import br.com.tick.tickdesck.config.email.dto.EmailCallDataDto;
import br.com.tick.tickdesck.config.llm.GeminiAiService;
import br.com.tick.tickdesck.models.call.application.CallService;
import br.com.tick.tickdesck.models.call.application.dto.CreateCallDto;
import br.com.tick.tickdesck.models.call.application.dto.UrgenciaCallDto;
import br.com.tick.tickdesck.models.call.domain.CallsEntity;
import br.com.tick.tickdesck.models.call.infra.CallRepository;
import br.com.tick.tickdesck.models.requisitantes.domain.RequisitanteEntity;
import br.com.tick.tickdesck.models.requisitantes.repository.RequisitanteRepository;
import br.com.tick.tickdesck.models.team.domain.TeamEntity;
import br.com.tick.tickdesck.models.team.infra.TeamRepository;
import br.com.tick.tickdesck.models.auditoria_call.application.ActionService;
import br.com.tick.tickdesck.models.auditoria_call.application.dto.RoleStatusAction;
import br.com.tick.tickdesck.models.user_externo.domain.UserExternoEntity;
import br.com.tick.tickdesck.models.user_interno.domain.UserEntity;
import br.com.tick.tickdesck.models.user_interno.infra.UserRepository;
import jakarta.mail.Address;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger; // ✅ CORRETO - SLF4J
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmailCallProcessorService {
    private static final Logger log = LoggerFactory.getLogger(EmailCallProcessorService.class);

    @Autowired
    private LocalEmailParseService localEmailParserService;

    @Autowired
    private GeminiAiService geminiAiService;

    @Autowired
    private EmailParsingService emailParsingService;

    @Autowired
    private CallService callService;

    @Autowired
    private RequisitanteRepository requisitanteRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CallRepository callRepository;

    @Autowired
    private ActionService actionService;

    @Value("${email.call.default.team.id:1}")
    private Long defaultTeamId;

    @Value("${email.call.default.user.responsavel.id:#{null}}")
    private Long defaultUserResponsavelId;

    @Value("${email.call.use.ai:true}")
    private boolean useAi;

    @Value("${email.call.default.action.user.id:#{null}}")
    private Long defaultActionUserId;


    //Processa um email recebido e cria um chamado automaticamente

    @Transactional
    public CallsEntity processEmailAndCreateCall(MimeMessage message) {
        try {
            // 1. Extrair informações básicas do email
            String from = extractEmailAddress(message.getFrom());
            String subject = message.getSubject();
            String body = emailParsingService.parseEmailContent(message);

            log.info("📧 Processando email para criar chamado | De: {} | Assunto: {}", from, subject);

            // Usar IA ou filtro de palavras-chave baseado na configuração
            EmailCallDataDto callData;
            if (useAi) {
                try {
                    callData = geminiAiService.extractCallDataFromEmail(from, subject, body);
                    log.info("🤖 Dados extraídos via Gemini AI | Título: {} | Urgência: {}",
                            callData.getTitle(), callData.getUrgency());
                } catch (Exception e) {
                    log.warn("⚠️ Erro ao usar Gemini AI, usando parser local como fallback: {}", e.getMessage());
                    callData = localEmailParserService.extractCallDataFromEmail(from, subject, body);
                    log.info("🔧 Dados extraídos via filtro de palavras-chave (fallback) | Título: {} | Urgência: {}",
                            callData.getTitle(), callData.getUrgency());
                }
            } else {
                callData = localEmailParserService.extractCallDataFromEmail(from, subject, body);
                log.info("🔧 Dados extraídos via filtro de palavras-chave | Título: {} | Urgência: {}",
                        callData.getTitle(), callData.getUrgency());
            }

            RequisitanteEntity requisitante = findOrCreateRequisitante(callData);

            TeamEntity team = teamRepository.findById(defaultTeamId)
                    .orElseThrow(() -> new RuntimeException("Equipe padrão não encontrada"));

            UserEntity userResponsavel = null;
            if (defaultUserResponsavelId != null) {
                userResponsavel = userRepository.findById(defaultUserResponsavelId)
                        .orElse(null);
            }

            UrgenciaCallDto urgencia = convertUrgencia(callData.getUrgency());

            Integer ultimoNumero = callRepository.findLastNumeroByEmpresa(team.getEnterprise().getId());
            int novoNumero = (ultimoNumero != null ? ultimoNumero : 0) + 1;

            CreateCallDto createCallDto = new CreateCallDto(
                    novoNumero,
                    callData.getTitle(),
                    requisitante.getId(),
                    userResponsavel != null ? userResponsavel.getId() : null,
                    true,
                    team.getId(),
                    urgencia,
                    LocalDateTime.now(),
                    null,
                    true
            );

            CallsEntity call = callService.createCall(createCallDto);

            log.info("✅ Chamado criado com sucesso | Número: {} | ID: {}",
                    call.getNumberCall(), call.getId());

            // Criar ação com o corpo do email e soluções sugeridas
            createActionFromEmailBody(call, body, callData, userResponsavel);

            return call;

        } catch (Exception e) {
            log.error("❌ Erro ao processar email e criar chamado", e);
            throw new RuntimeException("Falha ao criar chamado a partir do email", e);
        }
    }

    private String extractEmailAddress(Address[] addresses) throws Exception {
        if (addresses == null || addresses.length == 0) {
            throw new RuntimeException("Email sem remetente");
        }

        InternetAddress address = (InternetAddress) addresses[0];
        return address.getAddress().toLowerCase();
    }


    //Busca requisitante existente ou cria um novo

    private RequisitanteEntity findOrCreateRequisitante(EmailCallDataDto callData) {
        String email = callData.getRequisitanteEmail().toLowerCase();

        Optional<RequisitanteEntity> existing = requisitanteRepository.findByEmailIgnoreCase(email);

        if (existing.isPresent()) {
            log.debug("Requisitante encontrado: {}", email);
            return existing.get();
        }

        UserExternoEntity novoRequisitante = new UserExternoEntity();
        novoRequisitante.setEmail(email);

        String nome = callData.getRequisitanteName();
        if (nome == null || nome.isEmpty()) {
            nome = email.split("@")[0];
        }
        novoRequisitante.setName(nome);


        novoRequisitante = requisitanteRepository.save(novoRequisitante);
        log.info("✨ Novo requisitante criado: {} ({})", nome, email);

        return novoRequisitante;
    }


    private UrgenciaCallDto convertUrgencia(String urgenciaStr) {
        if (urgenciaStr == null || urgenciaStr.isEmpty()) {
            return UrgenciaCallDto.MEDIA;
        }

        return switch (urgenciaStr.toUpperCase()) {
            case "ALTA" -> UrgenciaCallDto.ALTA;
            case "BAIXA" -> UrgenciaCallDto.BAIXA;
            default -> UrgenciaCallDto.MEDIA;
        };
    }

    //Determina a equipe com base no conteúdo do email
    public TeamEntity determineTeamByContent(String subject, String body) {
        String content = (subject + " " + body).toLowerCase();

        // Exemplo de roteamento por palavras-chave
        List<TeamEntity> teams = teamRepository.findAll();

        for (TeamEntity team : teams) {
            String teamName = team.getName().toLowerCase();
            if (content.contains(teamName) ||
                    content.contains(teamName.replace(" ", ""))) {
                return team;
            }
        }

        // Retorna equipe padrão
        return teamRepository.findById(defaultTeamId)
                .orElseThrow(() -> new RuntimeException("Equipe padrão não encontrada"));
    }

    //Cria uma ação no chamado com o corpo do email e soluções sugeridas
    private void createActionFromEmailBody(CallsEntity call, String emailBody, EmailCallDataDto callData, UserEntity userResponsavel) {
        try {
            // Busca o usuário tickdeskIa para ações automáticas de email
            Long userIdToUse = null;
            var tickDeskIaUser = userRepository.findByUsername("tickdeskIa");
            
            if (tickDeskIaUser.isPresent()) {
                userIdToUse = tickDeskIaUser.get().getId();
                log.debug("Usando usuário tickdeskIa para ação automática");
            } else {
                // Fallback: usa configurações alternativas se tickdeskIa não existir
                if (defaultActionUserId != null) {
                    userIdToUse = defaultActionUserId;
                } else if (userResponsavel != null) {
                    userIdToUse = userResponsavel.getId();
                } else if (defaultUserResponsavelId != null) {
                    userIdToUse = defaultUserResponsavelId;
                }
                
                if (userIdToUse == null) {
                    log.warn("⚠️ Não foi possível criar ação para o chamado {}: usuário tickdeskIa não encontrado e nenhum usuário alternativo configurado", call.getId());
                    return;
                } else {
                    log.warn("⚠️ Usuário tickdeskIa não encontrado, usando usuário alternativo ID: {}", userIdToUse);
                }
            }

            // Limpa o corpo do email removendo assinaturas, formatações desnecessárias e palavrões
            String cleanBody = cleanEmailBody(emailBody);

            // Monta o corpo da ação com descrição e soluções sugeridas
            StringBuilder actionBody = new StringBuilder();
            
            // Adiciona a descrição do problema (já processada pela IA, mas pode vir do email original se IA falhar)
            if (callData.getDescription() != null && !callData.getDescription().trim().isEmpty()) {
                actionBody.append("📋 DESCRIÇÃO DO PROBLEMA:\n");
                actionBody.append(callData.getDescription().trim());
                actionBody.append("\n\n");
            } else {
                // Fallback: usa o corpo limpo do email se não houver descrição processada
                actionBody.append("📧 CORPO DO EMAIL:\n");
                actionBody.append(cleanBody);
                actionBody.append("\n\n");
            }

            // Adiciona soluções sugeridas pela IA (se disponíveis)
            if (callData.getSuggestedSolutions() != null && !callData.getSuggestedSolutions().isEmpty()) {
                actionBody.append("💡 SOLUÇÕES SUGERIDAS:\n");
                int solutionNumber = 1;
                for (String solution : callData.getSuggestedSolutions()) {
                    if (solution != null && !solution.trim().isEmpty()) {
                        actionBody.append(String.format("%d. %s\n", solutionNumber++, solution.trim()));
                    }
                }
            }

            // Se o corpo da ação ficar muito grande, trunca
            String finalActionBody = actionBody.toString().trim();
            if (finalActionBody.length() > 10000) {
                finalActionBody = finalActionBody.substring(0, 9997) + "...";
            }

            // Cria a ação com o conteúdo formatado
            actionService.createActionWithUserId(
                    call.getId(),
                    finalActionBody.isEmpty() ? cleanBody : finalActionBody,
                    userIdToUse,
                    RoleStatusAction.PUBLIC
            );

            log.info("📝 Ação criada no chamado {} com descrição e soluções sugeridas", call.getId());
        } catch (Exception e) {
            log.error("❌ Erro ao criar ação no chamado {}: {}", call.getId(), e.getMessage(), e);
            // Não lança exceção para não impedir a criação do chamado
        }
    }

    //Limpa o corpo do email para uso na ação - remove assinaturas, formatações e palavrões
    private String cleanEmailBody(String body) {
        if (body == null || body.trim().isEmpty()) {
            return "Corpo do email vazio.";
        }

        String cleanBody = body;

        // Remove assinaturas comuns de email
        cleanBody = cleanBody
                .replaceAll("(?i)\\n--\\s*\\n.*", "") // Remove assinatura após --
                .replaceAll("(?i)enviado do meu .*", "") // Remove "Enviado do meu iPhone"
                .replaceAll("(?i)sent from my .*", "")
                .replaceAll("(?i)^(re:|fwd:|fw:|enc:)\\s*", "") // Remove prefixos de reenvio
                .trim();

        // Remove palavrões e linguagem inadequada (fallback caso a IA não tenha removido)
        cleanBody = removeProfanity(cleanBody);

        // Se o corpo ficou muito grande, trunca
        if (cleanBody.length() > 5000) {
            cleanBody = cleanBody.substring(0, 4997) + "...";
        }

        return cleanBody.isEmpty() ? "Corpo do email vazio após limpeza." : cleanBody;
    }

    //Remove palavrões e linguagem inadequada do texto (fallback caso a IA não remova)
    private String removeProfanity(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Lista de palavrões comuns em português (filtro de fallback)
        // Padrões específicos de palavras ofensivas para remover/substituir
        String[] profanityPatterns = {
                // Palavrões comuns (usando regex case-insensitive com word boundaries)
                "(?i)\\b(porra|caralho|merda|foda|fdp|pqp|vtnc|cu|buceta|piroca|rola)\\b",
                // Expressões ofensivas comuns
                "(?i)\\b(vai\\s+se\\s+foder|vai\\s+se\\s+fuder|vai\\s+tomar|vtnc|pqp)\\b",
                // Palavras isoladas ofensivas comuns
                "(?i)\\bputa(?!rio|ridade|nho|ca|do)\\b"  // "puta" mas não "computador" ou "putativo"
        };

        // Substitui palavrões por termos neutros ou remove
        String cleaned = text;
        for (String pattern : profanityPatterns) {
            // Substitui por espaço para manter o fluxo do texto
            cleaned = cleaned.replaceAll(pattern, "");
        }

        // Remove múltiplos espaços criados após remoções
        cleaned = cleaned.replaceAll("\\s{2,}", " ").trim();

        // Remove linhas vazias excessivas
        cleaned = cleaned.replaceAll("\\n{3,}", "\n\n");

        return cleaned;
    }
}
