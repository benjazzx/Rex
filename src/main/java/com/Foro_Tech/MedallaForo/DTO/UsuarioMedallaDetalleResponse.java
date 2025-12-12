package com.Foro_Tech.MedallaForo.DTO;

import java.util.List;
import lombok.Data;

@Data
public class UsuarioMedallaDetalleResponse {

    private Long idUser;
    private String nombre;
    private String correo;
    private Integer puntajeTotal;
    private String estado;
    private List<String> medallas;   // ["BRONCE", "PLATA", ...]
}
