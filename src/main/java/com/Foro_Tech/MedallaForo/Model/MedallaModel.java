package com.Foro_Tech.MedallaForo.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "medallas")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de Medalla")



public class MedallaModel {

  @Id
    @Schema(description = "ID de la medalla", example = "1")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; 

    @Schema(description = "Nombre de la medalla", example = "Medalla de Oro")
    @Column(nullable = false, unique = true)
     private String nombre;
     

    @Schema(description = "Foto", example = "Foto de la medalla (bronce/plata/oro)")
    @Column(nullable = true)
     private String fotoLogo;


    
    @Schema(description = "Puntaje requerido para obtener la medalla", example = "100")
    @Column(nullable = false)
     private Integer puntajeRequerido;


}