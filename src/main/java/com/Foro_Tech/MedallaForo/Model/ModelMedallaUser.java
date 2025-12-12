package com.Foro_Tech.MedallaForo.Model;

import java.text.DateFormat;

import org.hibernate.type.descriptor.DateTimeUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medalla_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo que representa la asignación de una medalla a un usuario")
public class ModelMedallaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro en medalla_user", example = "1")
    private Long idMu;

    @Column(name = "id_user", nullable = false)
    @Schema(description = "ID del usuario que obtiene la medalla", example = "10")
    private Long idUser;

    @Column(name = "id_medalla", nullable = false)
    @Schema(description = "ID de la medalla asignada al usuario", example = "3")
    private Long idMedalla;

    @Column(name = "fecha_obtencion", nullable = false)
    @Schema(description = "Fecha en la que se asignó la medalla al usuario", example = "2025-11-20T14:30:00")
    private String fechaObtencion;
}
