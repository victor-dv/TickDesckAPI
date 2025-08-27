package br.com.tick.tickdesck.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTUserService {

    @Value("${security.token.secret.user}")
    private String secretKey;

    //Metodo para gerar o token JWT
    // Retorna o token JWT gerado
    // Se ocorrer algum erro, retorna null
    public String validadeToken(String token){
        try {
            token = token.replace("Bearer ", "");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getSubject();

        }catch (JWTVerificationException e){
            System.err.println("Invalid JWT token: " + e.getMessage());
            return null;
        }catch (Exception e) {
            System.err.println("Error validating JWT token: " + e.getMessage());
            return null;
        }
    }
}
