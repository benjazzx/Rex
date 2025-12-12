package com.Foro_Tech.MedallaForo.Repository;

import com.Foro_Tech.MedallaForo.Model.MedallaModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MedallaRepository extends JpaRepository<MedallaModel, Long> {

    Optional<MedallaModel> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    // Medallas cuyo requisito es <= puntos del usuario
    List<MedallaModel> findByPuntajeRequeridoLessThanEqual(Integer puntaje);
}
