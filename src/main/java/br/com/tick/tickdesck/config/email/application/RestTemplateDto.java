package br.com.tick.tickdesck.config.email.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateDto {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
