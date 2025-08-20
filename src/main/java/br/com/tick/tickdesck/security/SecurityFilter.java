package br.com.tick.tickdesck.security;

import br.com.tick.tickdesck.application.JWTUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// Filtro de segurança que intercepta cada requisição HTTP
// Verifica se o cabeçalho de autorização contém um token JWT válido
// Se o token for válido, autentica o usuário no contexto de segurança
// Se o token for inválido ou ausente, responde com status 401 (Não autorizado)
@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Injetando o serviço de validação de token JWT
    @Autowired
    private JWTUserService jwtUserService;

    // Método que intercepta cada requisição HTTP
    // Verifica se o cabeçalho de autorização contém um token JWT válido
    // Se o token for válido, autentica o usuário no contexto de segurança
    // Se o token for inválido ou ausente, responde com status 401 (Não autorizado)
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho de autorização está presente
        if (authHeader != null) {
            try {
                String token = authHeader.replace("Bearer ", "").trim();
                String userId = jwtUserService.validadeToken(token);

                // Se o token for inválido, responde com status 401
                if (userId == null || userId.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or expired token");
                    return;
                }

                // Cria um objeto de autenticação com o ID do usuário
                // e o define no contexto de segurança
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Em caso de erro na validação do token, responde com status 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token: " + e.getMessage());
                return;
            }
        }
        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);

    }

}
