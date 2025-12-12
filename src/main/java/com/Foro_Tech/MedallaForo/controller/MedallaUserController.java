package com.Foro_Tech.MedallaForo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Foro_Tech.MedallaForo.Model.MedallaModel;
import com.Foro_Tech.MedallaForo.Model.ModelMedallaUser;
import com.Foro_Tech.MedallaForo.Service.MedallaUserService;
import com.Foro_Tech.MedallaForo.DTO.UsuarioMedallaDetalleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/medalla-user")
public class MedallaUserController {

    @Autowired
    private MedallaUserService medallaUserService;


    // ======================================================================
    // 1. LISTAR MEDALLAS ASIGNADAS A UN USUARIO (RELACIÓN CRUDA)
    // ======================================================================
    // POSTMAN → GET
    // URL:
    //   http://localhost:8082/api/v1/medalla-user/usuario/1
    //
    // Retorna TODAS las filas idUser - idMedalla que ya están en la BD.
    // Ojo: aquí NO se evalúa nada nuevo, solo se muestra lo que ya existe.
    // ======================================================================
    @Operation(summary = "Obtiene todas las medallas asignadas a un usuario (lo que ya está en BD)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medallas encontradas",
            content = @Content(schema = @Schema(implementation = ModelMedallaUser.class))),
        @ApiResponse(responseCode = "204", description = "El usuario no tiene medallas asignadas")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> listarMedallasUsuario(@PathVariable Long idUsuario) {

        List<ModelMedallaUser> lista = medallaUserService.obtenerMedallasDeUsuario(idUsuario);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }


    // ======================================================================
    // 2. ELIMINAR UNA MEDALLA ASIGNADA (OPCIONAL, ADMIN)
    // ======================================================================
    // POSTMAN → DELETE
    // URL:
    //   http://localhost:8082/api/v1/medalla-user/5
    //
    // Elimina una fila de relación por su ID interno (idMedallaUser / idMu).
    // Puedes dejarlo como endpoint de administración.
    // ======================================================================
    @Operation(summary = "Elimina una medalla asignada por su ID interno (idMedallaUser)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{idMedallaUser}")
    public ResponseEntity<?> eliminarMedalla(@PathVariable Long idMedallaUser) {

        boolean eliminado = medallaUserService.eliminarMedallaUsuario(idMedallaUser);

        if (!eliminado) {
            return ResponseEntity.status(404).body("Registro no encontrado.");
        }

        return ResponseEntity.noContent().build();
    }


    // ======================================================================
    // 3. DETALLE AUTOMÁTICO: USUARIO + MEDALLAS
    // ======================================================================
    // POSTMAN → GET
    //   http://localhost:8082/api/v1/medalla-user/detalle/1
    //
  
    @Operation(summary = "Obtiene detalle del usuario y asigna automáticamente las medallas según su puntaje actual")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle obtenido y medallas evaluadas correctamente",
            content = @Content(schema = @Schema(implementation = UsuarioMedallaDetalleResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado en UsuarioMS"),
        @ApiResponse(responseCode = "500", description = "Error al procesar la información")
    })
    @GetMapping("/detalle/{idUsuario}")
    public ResponseEntity<?> obtenerDetalleConMedallas(@PathVariable Long idUsuario) {

        try {
            UsuarioMedallaDetalleResponse detalle =
                    medallaUserService.obtenerDetalleUsuarioConMedallas(idUsuario);

            if (detalle == null) {
                return ResponseEntity.status(404).body("Usuario no encontrado en UsuarioMS.");
            }

            return ResponseEntity.ok(detalle);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error al consultar información del usuario: " + e.getMessage());
        }
    }


    @GetMapping("/desbloqueables/{puntos}")
public ResponseEntity<List<MedallaModel>> medallasDesbloqueables(@PathVariable Integer puntos) {
    return ResponseEntity.ok(medallaUserService.obtenerDesbloqueables(puntos));
}

}
