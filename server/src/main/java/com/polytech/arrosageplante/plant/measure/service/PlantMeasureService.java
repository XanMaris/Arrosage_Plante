package com.polytech.arrosageplante.plant.measure.service;

import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.measure.domain.PlantMeasure;
import com.polytech.arrosageplante.plant.measure.repository.PlantHumidityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlantMeasureService {
    private final PlantHumidityRepository plantHumidityRepository;

    public PlantMeasureService(PlantHumidityRepository plantHumidityRepository) {
        this.plantHumidityRepository = plantHumidityRepository;
    }

    public PlantMeasure getMostRecentMeasure(Plant plant) {
        return this.plantHumidityRepository.findTopByPlantOrderByMeasureDateDesc(plant).orElseThrow(() -> new RuntimeException("Pas de mesure récente pour cette plante"));
    }

    public void saveMeasure(PlantMeasure measure) {
        this.plantHumidityRepository.save(measure);
    }

    public List<PlantMeasure> getDailyMeasures(Plant plant) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        LocalDateTime end = start.plusDays(1);
        return this.plantHumidityRepository.findAllByPlantAndMeasureDateAfterAndMeasureDateBefore(plant, start, end);
    }
}
