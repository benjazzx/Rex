package com.Foro_Tech.MedallaForo.WebClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${usuario.service.url:http://187.33.145.238:8084}")
    private String usuarioServiceUrl;

    @Bean
    public WebClient usuarioWebClient() {
        // Asegurarse que la URL base tenga el prefijo /api/v1 que espera WebUser
        String base = usuarioServiceUrl.endsWith("/api/v1") ? usuarioServiceUrl : usuarioServiceUrl + "/api/v1";
        return WebClient.builder()
                .baseUrl(base)
                .build();
    }
}
