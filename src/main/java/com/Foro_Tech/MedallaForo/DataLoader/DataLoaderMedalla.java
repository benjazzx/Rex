package com.Foro_Tech.MedallaForo.DataLoader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.Foro_Tech.MedallaForo.Model.MedallaModel;
import com.Foro_Tech.MedallaForo.Repository.MedallaRepository;

@Configuration
public class DataLoaderMedalla {

    @Bean
    public CommandLineRunner precargarMedallas(MedallaRepository repo) {
        return args -> {

            if (repo.count() == 0) {
                // Medalla(nombre, foto, puntaje, puntajeRequerido)
                repo.save(new MedallaModel(null, "BRONCE", "bronce.png",  25));
                repo.save(new MedallaModel(null, "PLATA", "plata.png",  50));
                repo.save(new MedallaModel(null, "ORO", "oro.png",  100));

                System.out.println(">>> Medallas precargadas correctamente.");
            } else {
                System.out.println(">>> Medallas ya existen, no se cargaron nuevas.");
            }
        };
    }
}
