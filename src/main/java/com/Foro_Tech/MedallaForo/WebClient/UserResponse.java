package com.Foro_Tech.MedallaForo.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserResponse {
    private Long idUser;
    private String nombre;
    private String correo;
    private Integer puntajeTotal;
    private String estado;
}
