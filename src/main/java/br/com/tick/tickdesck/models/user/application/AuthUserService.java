package br.com.tick.tickdesck.models.user.application;

import br.com.tick.tickdesck.models.user.application.dto.AuthUserRequestDto;
import br.com.tick.tickdesck.models.user.application.dto.AuthUserResponseDto;
import br.com.tick.tickdesck.models.user.infra.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthUserService {

    // O valor da chave secreta é injetado a partir do arquivo de propriedades
    @Value("${security.token.secret.user}")
    private String secretKey;
    // O PasswordEncoder é injetado para verificar a senha do usuário
    @Autowired
    private PasswordEncoder passwordEncoder;
    // O repositório de usuários é injetado para acessar os dados do usuário
    @Autowired
    private UserRepository userRepository;

    // Método que executa a autenticação do usuário
    /* Recebe um objeto AuthUserRequestDto contendo o email e a senha do usuário
    * Verifica se o usuário existe no repositório pelo email
    * Se o usuário não existir ou a senha não corresponder, lança uma exceção
    * Se a autenticação for bem-sucedida, gera um token JWT com as informações
    * do usuário e o retorna encapsulado em um objeto AuthUserResponseDto
    * O token contém informações como o ID do usuário, nome, papel e email,
    * além de uma data de expiração definida para 80 minutos a partir do momento da
   autenticação. */
    public AuthUserResponseDto execute(AuthUserRequestDto authUserRequestDto) {
        var user = this.userRepository.findByEmail(authUserRequestDto.email())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));
        var passwordMatch = this.passwordEncoder
                .matches(authUserRequestDto.password(), user.getPassword());
        if (!passwordMatch) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        System.out.println(secretKey);
        // Cria o algoritmo de assinatura usando a chave secreta
        // e define o tempo de expiração do token
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expireIn = Instant.now().plus(Duration.ofMinutes(80));
        var token = JWT.create()
                .withIssuer("TickDesk")
                .withSubject(user.getId().toString())
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole().name())
                .withClaim("email", user.getEmail())
                .withExpiresAt(java.util.Date.from(expireIn))
                .sign(algorithm);

        /* Retorna um objeto AuthUserResponseDto contendo o token e o tempo de expiração
         do token em milissegundos desde a época (epoch) */
        return AuthUserResponseDto.builder()
                .access_token(token)
                .expires_in(expireIn.toEpochMilli())
                .build();

    }


}
