package com.Foro_Tech.MedallaForo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Foro_Tech.MedallaForo.Model.MedallaModel;
import com.Foro_Tech.MedallaForo.Service.MedallaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/medallas")
public class MedallaController {

    @Autowired
    private MedallaService medallaService;


    // ------------------------------------------------------
    // CREAR MEDALLA
    // POSTMAN → POST
    // URL: http://localhost:8082/api/v1/medallas
    //
    // BODY (JSON):
    // {
    //   "nombre": "Principiante",
    //   "fotoLogo": "url.png",
    //   "puntajeRequerido": 50
    // }
    // ------------------------------------------------------
    @Operation(summary = "Crear una nueva medalla")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Medalla creada correctamente",
            content = @Content(schema = @Schema(implementation = MedallaModel.class))),
        @ApiResponse(responseCode = "409", description = "Ya existe una medalla con ese nombre")
    })
    @PostMapping
    public ResponseEntity<?> crearMedalla(@RequestBody MedallaModel nueva) {

        MedallaModel creada = medallaService.crearMedalla(nueva);

        if (creada == null) {
            return ResponseEntity.status(409).body("La medalla ya existe");
        }

        return ResponseEntity.status(201).body(creada);
    }


    @Operation(summary = "Evaluar si un usuario recibe una nueva medalla")
@PostMapping("/evaluar/{idUsuario}/{puntajeTotal}")
public ResponseEntity<?> evaluarMedallas(
        @PathVariable Long idUsuario,
        @PathVariable int puntajeTotal) {

    medallaService.asignarMedalla(idUsuario, puntajeTotal);

    return ResponseEntity.ok("Evaluación completada");
}

    // ------------------------------------------------------
    // OBTENER TODAS LAS MEDALLAS
    // POSTMAN → GET
    // URL: http://localhost:8082/api/v1/medallas
    // ------------------------------------------------------
    @Operation(summary = "Obtener todas las medallas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista generada correctamente",
            content = @Content(schema = @Schema(implementation = MedallaModel.class))),
        @ApiResponse(responseCode = "204", description = "No hay medallas registradas")
    })
    @GetMapping
    public ResponseEntity<List<MedallaModel>> obtenerMedallas() {

        List<MedallaModel> lista = medallaService.obtenerMedallas();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }


    // ------------------------------------------------------
    // OBTENER MEDALLA POR ID
    // POSTMAN → GET
    // URL: http://localhost:8082/api/v1/medallas/{id}
    //
    // EJEMPLO:
    // http://localhost:8082/api/v1/medallas/1
    // ------------------------------------------------------
    @Operation(summary = "Buscar una medalla por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Medalla encontrada",
            content = @Content(schema = @Schema(implementation = MedallaModel.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró la medalla")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedallaModel> obtenerPorId(@PathVariable Long id) {

        MedallaModel medalla = medallaService.obtenerPorId(id);

        if (medalla == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(medalla);
    }


    // ------------------------------------------------------
    // ACTUALIZAR MEDALLA
    // POSTMAN → PUT
    // URL: http://localhost:8082/api/v1/medallas/{id}
    //
    // BODY (JSON):
    // {
    //   "nombre": "Experto",
    //   "fotoLogo": "nueva.png",
    //   "puntajeRequerido": 200
    // }
    // ------------------------------------------------------
    @Operation(summary = "Actualizar una medalla por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Medalla actualizada correctamente",
            content = @Content(schema = @Schema(implementation = MedallaModel.class))),
        @ApiResponse(responseCode = "404", description = "No existe la medalla")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedallaModel> actualizar(
            @PathVariable Long id,
            @RequestBody MedallaModel actualizada) {

        MedallaModel medalla = medallaService.actualizarMedalla(id, actualizada);

        if (medalla == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(medalla);
    }


    // ------------------------------------------------------
    // ELIMINAR MEDALLA
    // POSTMAN → DELETE
    // URL: http://localhost:8082/api/v1/medallas/{id}
    //
    // EJEMPLO:
    // http://localhost:8082/api/v1/medallas/3
    // ------------------------------------------------------
    @Operation(summary = "Eliminar una medalla por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Medalla eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "La medalla no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        boolean eliminada = medallaService.eliminarMedalla(id);

        if (!eliminada) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    

}
