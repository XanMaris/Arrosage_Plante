package com.polytech.arrosageplante.plant.service;

import com.polytech.arrosageplante.esp32.communication.websocket.exception.FirstSynchronizationFailedException;
import com.polytech.arrosageplante.esp32.communication.websocket.output.WebSocketPublisherService;
import com.polytech.arrosageplante.exception.EntityNotFound;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.exception.EditionException;
import com.polytech.arrosageplante.plant.repository.PlantRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlantCrudService {
    private final PlantRepository plantRepository;
    private final WebSocketPublisherService webSocketPublisherService;

    public PlantCrudService(PlantRepository plantRepository, WebSocketPublisherService webSocketPublisherService) {
        this.plantRepository = plantRepository;
        this.webSocketPublisherService = webSocketPublisherService;
    }

    public List<Plant> getPlants() {
        return this.plantRepository.findAll();
    }

    public Plant getPlant(String id) {
        Optional<Plant> plant = this.plantRepository.findById(id);

        if (plant.isEmpty()) {
            throw new EntityNotFound();
        }

        return plant.get();
    }

    public Plant addPlant(PlantAddDTO plantAddDTO) throws IOException {
        MultipartFile imageFile = plantAddDTO.image();
        byte[] imageData = imageFile != null && !imageFile.isEmpty() ? imageFile.getBytes() : null;

        String imageContentType = imageFile != null && !imageFile.isEmpty() ? imageFile.getContentType() : null;

        Plant plant = new Plant(plantAddDTO.name(),
                plantAddDTO.description(),
                plantAddDTO.autoWatering(),
                plantAddDTO.waterByDayPercentage(),
                imageData,
                plantAddDTO.waterRetentionCoefficient(),
                imageContentType
        );

        Plant savedPlant = this.plantRepository.save(plant);

        try {
            String privateCode = this.webSocketPublisherService.associatePlantIdToEsp32(plantAddDTO.privateEsp32Code(), savedPlant.getId().toString());
            savedPlant.setEsp32PrivateCode(privateCode);
            savedPlant = this.plantRepository.save(plant);
        } catch (FirstSynchronizationFailedException e) {
            throw e; // TODO: faire remonter un message d'erreur, pour indiquer au front qu'il y a une erreur de syncro entre l'esp et le serveur
        }

        return savedPlant;
    }

    public Plant edit(String id, double newHumidityBaseRate, boolean automaticWatering) {
        Plant plantToEdit = this.plantRepository.findById(id).orElseThrow(EditionException::new);
        plantToEdit.setWaterByDayPercentage(newHumidityBaseRate);
        plantToEdit.setAutomaticWatering(automaticWatering);
        return this.plantRepository.save(plantToEdit);
    }

    public void deletePlant(String  id) {
        Optional<Plant> plant = this.plantRepository.findById(id);

        if (plant.isEmpty()) {
            throw new EntityNotFound();
        }

        this.plantRepository.deleteById(id);

        this.webSocketPublisherService.disassociatePlantIdToEsp32(plant.get().getId());
    }
}
