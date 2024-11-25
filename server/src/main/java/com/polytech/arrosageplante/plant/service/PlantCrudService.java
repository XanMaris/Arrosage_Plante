package com.polytech.arrosageplante.plant.service;

import com.polytech.arrosageplante.exception.EntityNotFound;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.repository.PlantRepository;
import com.polytech.arrosageplante.esp32.HumidityRateSensorEsp32Service;
import com.polytech.arrosageplante.plant.sensor.domain.Sensor;
import com.polytech.arrosageplante.plant.sensor.service.SensorCrudService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlantCrudService {
    private final PlantRepository plantRepository;
    private final HumidityRateSensorEsp32Service humidityRateSensorEsp32Service;
    private final SensorCrudService sensorCrudService;


    public PlantCrudService(PlantRepository plantRepository,
                            HumidityRateSensorEsp32Service humidityRateSensorEsp32Service,
                            SensorCrudService sensorCrudService) {
        this.plantRepository = plantRepository;
        this.humidityRateSensorEsp32Service = humidityRateSensorEsp32Service;
        this.sensorCrudService = sensorCrudService;
    }

    public List<Plant> getPlants() {
        return this.plantRepository.findAll();
    }

    public Plant getPlant(String id) {
        Optional<Plant> plant = this.plantRepository.findById(id);

        if (plant.isEmpty()) {
            throw new EntityNotFound();
        }

        // TODO: faire une requête HTTP vers l'ESP-32 pour récupérer les détails des capteurs associées à la plante

        return plant.get();
    }

    public Plant addPlant(PlantAddDTO plantAddDTO) throws IOException {
        Sensor humidityRateSensor = this.sensorCrudService.getSensor(plantAddDTO.humidityRateSensorId());

        double currentHumidityRate = this.humidityRateSensorEsp32Service.getCurrentHumidityRate(humidityRateSensor.getId());

        MultipartFile imageFile = plantAddDTO.image();
        byte[] imageData = imageFile != null && !imageFile.isEmpty() ? imageFile.getBytes() : null;

        String imageContentType = imageFile != null && !imageFile.isEmpty() ? imageFile.getContentType() : null;

        Plant plant = new Plant(plantAddDTO.name(),
                plantAddDTO.description(),
                currentHumidityRate,
                plantAddDTO.autoWatering(),
                plantAddDTO.waterByDayPercentage(),
                imageData,
                plantAddDTO.waterRetentionCoefficient(),
                imageContentType
        );
        plant.addHumiditySensor(humidityRateSensor);

        return this.plantRepository.save(plant);
    }

    public void deletePlant(String  id) {
        Optional<Plant> plant = this.plantRepository.findById(id);

        if (plant.isEmpty()) {
            throw new EntityNotFound();
        }

        this.plantRepository.deleteById(id);

        // TODO: Faire une requête HTTP vers l'ESP-32 pour effectuer des actions supplémentaires, comme éteindre un système d'irrigation ou autre
    }
}
