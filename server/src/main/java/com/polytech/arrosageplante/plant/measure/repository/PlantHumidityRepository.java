package com.polytech.arrosageplante.plant.measure.repository;

import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.measure.domain.PlantMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlantHumidityRepository extends JpaRepository<PlantMeasure, String> {
    /**
     * Récupère la mesure d'humidité la plus récente pour une plante donnée.
     *
     * @param plant La plante dont on veut la mesure d'humidité la plus récente.
     * @return Une Optional contenant la mesure d'humidité la plus récente, ou vide si aucune mesure n'existe.
     */
    Optional<PlantMeasure> findTopByPlantOrderByMeasureDateDesc(Plant plant);
    List<PlantMeasure> findAllByPlantAndMeasureDateAfterAndMeasureDateBefore(Plant plant, LocalDateTime start, LocalDateTime end);
}
