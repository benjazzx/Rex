package com.Foro_Tech.MedallaForo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Foro_Tech.MedallaForo.Model.MedallaModel;
import com.Foro_Tech.MedallaForo.Model.ModelMedallaUser;

@Repository
public interface MedallaUserRepository extends JpaRepository<ModelMedallaUser, Long> {

    boolean existsByIdUserAndIdMedalla(Long idUser, Long idMedalla);

    
    java.util.List<ModelMedallaUser> findByIdUser(Long idUser);


}
