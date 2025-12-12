package com.Foro_Tech.MedallaForo.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Foro_Tech.MedallaForo.Model.MedallaModel;
import com.Foro_Tech.MedallaForo.Model.ModelMedallaUser;
import com.Foro_Tech.MedallaForo.Repository.MedallaRepository;
import com.Foro_Tech.MedallaForo.Repository.MedallaUserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedallaService {

        @Autowired
        private MedallaUserRepository medallaUserRepository;

    @Autowired
    private MedallaRepository medallaRepository;

    // traer todas las medallas registradas
    public List<MedallaModel> obtenerMedallas() {
        return medallaRepository.findAll();
    }

    // crear una medalla nueva (aquí reviso que no exista otra con el mismo nombre)
    public MedallaModel crearMedalla(MedallaModel nueva) {
        if (medallaRepository.existsByNombre(nueva.getNombre())) {
            return null;  // si ya existe, devuelvo null y el controller manejará el error
        }
        return medallaRepository.save(nueva);
    }

    // buscar medalla por id
    public MedallaModel obtenerPorId(Long id) {
        return medallaRepository.findById(id).orElse(null);
    }

    // actualizar una medalla existente
    public MedallaModel actualizarMedalla(Long id, MedallaModel actualizada) {
        Optional<MedallaModel> optional = medallaRepository.findById(id);

        if (optional.isEmpty()) {
            return null; // si no existe, retorno null
        }

        MedallaModel existente = optional.get();
        existente.setNombre(actualizada.getNombre());
        existente.setPuntajeRequerido(actualizada.getPuntajeRequerido());
        existente.setFotoLogo(actualizada.getFotoLogo());

        return medallaRepository.save(existente);
    }

    // eliminar una medalla por su id
    public boolean eliminarMedalla(Long id) {
        if (medallaRepository.existsById(id)) {
            medallaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // obtener medallas cuyo puntaje requerido sea menor o igual al puntaje del usuario
    // esto sirve para saber qué medallas puede desbloquear un usuario
    public List<MedallaModel> obtenerPorPuntaje(Integer puntajeUsuario) {
        return medallaRepository.findByPuntajeRequeridoLessThanEqual(puntajeUsuario);
    }


    public List<MedallaModel> obtenerDesbloqueables(Integer puntosUsuario) {

    // Traigo todas las medallas
    List<MedallaModel> todas = medallaRepository.findAll();

    // Filtro las que el usuario puede desbloquear
    return todas.stream()
            .filter(m -> puntosUsuario >= m.getPuntajeRequerido())
            .toList();
}

public void asignarMedalla(Long idUsuario, int puntajeActual) {

    // 1) Obtener todas las medallas del sistema
    List<MedallaModel> todasLasMedallas = medallaRepository.findAll();

    // 2) Obtener medallas que ya tiene el usuario
    List<ModelMedallaUser> medallasUsuario = medallaUserRepository.findByIdUser(idUsuario);

    List<Long> idsYaAsignadas = medallasUsuario.stream()
            .map(mu -> mu.getIdMedalla())
            .toList();

    // 3) Evaluar cuáles medallas debe recibir según el puntaje
    for (MedallaModel medalla : todasLasMedallas) {

        // Ya tiene esta medalla
        if (idsYaAsignadas.contains(medalla.getId())) {
            continue;
        }

        // Cumple con el puntaje requerido
        if (puntajeActual >= medalla.getPuntajeRequerido()) {

            ModelMedallaUser nueva = new ModelMedallaUser();
            nueva.setIdUser(idUsuario);
            nueva.setIdMedalla(medalla.getId());
            nueva.setFechaObtencion(LocalDateTime.now().toString());

            medallaUserRepository.save(nueva);

            System.out.println("✔ Medalla asignada: " + medalla.getNombre());
        }
    }
}
}
