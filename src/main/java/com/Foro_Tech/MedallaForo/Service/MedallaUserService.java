package com.Foro_Tech.MedallaForo.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Foro_Tech.MedallaForo.DTO.UsuarioMedallaDetalleResponse;
import com.Foro_Tech.MedallaForo.Model.MedallaModel;
import com.Foro_Tech.MedallaForo.Model.ModelMedallaUser;
import com.Foro_Tech.MedallaForo.Repository.MedallaRepository;
import com.Foro_Tech.MedallaForo.Repository.MedallaUserRepository;
import com.Foro_Tech.MedallaForo.WebClient.WebUser;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedallaUserService {

     
 @Autowired
 private WebUser webUser;

    
    @Autowired
    private MedallaRepository medallaRepository;   

    @Autowired
    private MedallaUserRepository medallaUserRepository;

    // asignar medalla a un usuario
    public ModelMedallaUser asignarMedallaAUsuario(Long idUsuario, Long idMedalla) {

        boolean yaTiene = medallaUserRepository
                .existsByIdUserAndIdMedalla(idUsuario, idMedalla);

        if (yaTiene) {
            return null; // ya la tiene, no hago nada
        }

        ModelMedallaUser nuevo = new ModelMedallaUser();
        nuevo.setIdUser(idUsuario);
        nuevo.setIdMedalla(idMedalla);
        nuevo.setFechaObtencion(LocalDateTime.now().toString());

        return medallaUserRepository.save(nuevo);
    }

     public void evaluarYAsignarMedallas(Long idUsuario, int puntajeActual) {

        
        var medallasDesbloqueables =
                medallaRepository.findByPuntajeRequeridoLessThanEqual(puntajeActual);

        for (MedallaModel medalla : medallasDesbloqueables) {
            // Asigna la medalla si el usuario aún no la tiene
            asignarMedallaAUsuario(idUsuario, medalla.getId());
        }
    }

    



// ================================================================
// Obtener detalle completo de usuario + medallas automáticas
// ================================================================
public UsuarioMedallaDetalleResponse obtenerDetalleUsuarioConMedallas(Long idUsuario) {

    // 1) Consultar al microservicio Usuario
    var user = webUser.obtenerUsuarioPorId(idUsuario);
    if (user == null) {
        return null;
    }

    // Construyo el DTO ya inicializado con datos del usuario
    UsuarioMedallaDetalleResponse respuesta = new UsuarioMedallaDetalleResponse();
    respuesta.setIdUser(user.getIdUser());
    respuesta.setNombre(user.getNombre());
    respuesta.setCorreo(user.getCorreo());
    respuesta.setPuntajeTotal(user.getPuntajeTotal());
    respuesta.setEstado(user.getEstado());

    // Si el usuario está baneado / bloqueado, removeré sus medallas y devuelvo lista vacía
    String estado = user.getEstado();
    if (estado != null) {
        String es = estado.trim().toLowerCase();
        if (es.equals("baneado") || es.equals("banned") || es.equals("suspended") || es.equals("bloqueado") || es.equals("blocked")) {
            var relaciones = medallaUserRepository.findByIdUser(idUsuario);
            if (!relaciones.isEmpty()) {
                medallaUserRepository.deleteAll(relaciones);
            }
            respuesta.setMedallas(List.of());
            return respuesta;
        }
    }

    // Si no está baneado, evaluar y (si corresponde) asignar medallas según su puntaje
    int puntaje = user.getPuntajeTotal();
    var medallasAsignadas = evaluarYObtenerMedallas(idUsuario, puntaje);

    respuesta.setMedallas(
        medallasAsignadas.stream()
            .map(MedallaModel::getNombre)
            .toList()
    );

    return respuesta;
}

     
    public List<MedallaModel> obtenerMedallasDetalladasDeUsuario(Long idUsuario) {

        List<ModelMedallaUser> relaciones = medallaUserRepository.findByIdUser(idUsuario);
        if (relaciones.isEmpty()) {
            return List.of();
        }

        var idsMedallas = relaciones.stream()
                .map(ModelMedallaUser::getIdMedalla)
                .distinct()
                .toList();

        return medallaRepository.findAllById(idsMedallas);
    }

    // Atajo: evaluar + devolver medallas ya asignadas
    public List<MedallaModel> evaluarYObtenerMedallas(Long idUsuario, int puntajeActual) {
        evaluarYAsignarMedallas(idUsuario, puntajeActual);
        return obtenerMedallasDetalladasDeUsuario(idUsuario);
    }


    // listar medallas de un usuario
    public List<ModelMedallaUser> obtenerMedallasDeUsuario(Long idUsuario) {
        return medallaUserRepository.findByIdUser(idUsuario);
    }

    // eliminar una medalla asignada
    public boolean eliminarMedallaUsuario(Long idMedallaUser) {
        if (medallaUserRepository.existsById(idMedallaUser)) {
            medallaUserRepository.deleteById(idMedallaUser);
            return true;
        }
        return false;
    }

    public List<MedallaModel> obtenerDesbloqueables(Integer puntosUsuario) {
        List<MedallaModel> todas = medallaRepository.findAll();

        return todas.stream()
                .filter(m -> puntosUsuario >= m.getPuntajeRequerido())
                .toList();
    }
   
}
