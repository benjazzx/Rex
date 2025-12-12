package com.Foro_Tech.MedallaForo.WebClient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebUser {

    private final WebClient usuarioWebClient;

    public WebUser(@Qualifier("usuarioWebClient") WebClient usuarioWebClient) {
        this.usuarioWebClient = usuarioWebClient;
    }

    public UserResponse obtenerUsuarioPorId(Long idUser) {
        return usuarioWebClient.get()
                // OJO: usamos el endpoint interno, NO /usuarios/{id}
                .uri("/usuarios/internal/{id}", idUser)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }
}
