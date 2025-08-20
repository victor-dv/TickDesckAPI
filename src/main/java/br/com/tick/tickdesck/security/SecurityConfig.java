package br.com.tick.tickdesck.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

//Classe de configuração de segurança
//Define as regras de segurança para as requisições HTTP
//Adiciona o filtro de segurança personalizado
@Configuration
public class SecurityConfig {

    //Injeta o filtro de segurança personalizado
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //Desabilita CSRF para facilitar testes com Postman ou Insomnia
                .csrf(crsf -> crsf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                //Configura as autorizações das requisições
                //Permite acesso livre a /user/**
                //Exige autenticação para qualquer outra requisição
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                //Adiciona o filtro de segurança personalizado antes do filtro de autenticação básica
                .addFilterBefore(securityFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    //Define o codificador de senhas como BCrypt
    //BCrypt é um algoritmo de hash seguro para armazenar senhas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
