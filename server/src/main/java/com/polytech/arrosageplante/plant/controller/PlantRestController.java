package com.polytech.arrosageplante.plant.controller;

import com.polytech.arrosageplante.exception.EntityNotFound;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.repository.PlantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PlantRestController {
    private final PlantRepository plantRepository;


    public PlantRestController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }


    @GetMapping("plant")
    public List<Plant> getPlants() {
        return this.plantRepository.findAll();
    }

    @GetMapping("plant/{id}")
    public Plant getPlant(@PathVariable Long id) {
        Optional<Plant> plant = this.plantRepository.findById(id);

        if (plant.isEmpty()) {
            throw new EntityNotFound();
        }

        // TODO: faire une requête HTTP vers l'ESP-32 pour récupérer les détails des capteurs associées à la plante

        return plant.get();
    }

    @DeleteMapping("plant/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        Optional<Plant> plant = this.plantRepository.findById(id);

        if (plant.isEmpty()) {
            throw new EntityNotFound();
        }

        this.plantRepository.deleteById(id);

        // TODO: Faire une requête HTTP vers l'ESP-32 pour effectuer des actions supplémentaires, comme éteindre un système d'irrigation ou autre

        return ResponseEntity.noContent().build(); // Renvoie un statut HTTP 204 (No Content) si la suppression a réussi
    }
}
