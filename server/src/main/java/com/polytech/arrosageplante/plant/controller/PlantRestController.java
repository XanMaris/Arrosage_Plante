package com.polytech.arrosageplante.plant.controller;

import com.polytech.arrosageplante.esp32.communication.websocket.output.WebSocketPublisherService;
import com.polytech.arrosageplante.exception.EntityNotFound;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.measure.domain.PlantMeasure;
import com.polytech.arrosageplante.plant.measure.service.PlantMeasureService;
import com.polytech.arrosageplante.plant.service.PlantAddDTO;
import com.polytech.arrosageplante.plant.service.PlantCrudService;import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

@RequestMapping("/plant")
@RestController
public class PlantRestController {
    private final PlantCrudService plantCrudService;
    private final PlantMeasureService plantMeasureService;
    private final WebSocketPublisherService webSocketPublisherService;

    public PlantRestController(PlantCrudService plantCrudService,
                               PlantMeasureService plantMeasureService,
                               WebSocketPublisherService webSocketPublisherService) {
        this.plantCrudService = plantCrudService;
        this.plantMeasureService = plantMeasureService;
        this.webSocketPublisherService = webSocketPublisherService;
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    public Plant addPlant(@ModelAttribute PlantAddDTO plantAddDTO) throws IOException {
        return this.plantCrudService.addPlant(plantAddDTO);
    }

    @GetMapping("/")
    public List<Plant> getPlants() {
        return this.plantCrudService.getPlants();
    }

    @GetMapping("/{id}")
    public PlantDetailDto getPlant(@PathVariable String id) {
        Plant plant = this.plantCrudService.getPlant(id);
        PlantMeasure plantMeasureMostRecentMeasure = this.plantMeasureService.getMostRecentMeasure(plant);
        return PlantDetailDto.fromPlant(plant, plantMeasureMostRecentMeasure.getAirHumidity());
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPlantImage(@PathVariable String id) {
        Plant plant = this.plantCrudService.getPlant(id);
        byte[] image = plant.getImage();
        String imageContentType = plant.getImageContentType();

        if (image == null || imageContentType == null) {
            return ResponseEntity.notFound().build();
        }

        // Détecter le type MIME si nécessaire (optionnel)
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(imageContentType);
        } catch (IllegalArgumentException e) {
            // Si le type MIME est invalide, utiliser un type par défaut ou renvoyer une erreur
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image);
    }


    @PostMapping(value = "{id}/water")
    public void waterPlant(@PathVariable String id) {
        Plant plant = this.plantCrudService.getPlant(id);

        this.webSocketPublisherService.arroser(plant);
    }

    @GetMapping(value = "{id}/measures/day")
    public List<PlantMeasure> plantMeasures(@PathVariable String id) {
        Plant plant = this.plantCrudService.getPlant(id);
        return this.plantMeasureService.getDailyMeasures(plant);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable String id) {
        this.plantCrudService.deletePlant(id);

        return ResponseEntity.noContent().build(); // Renvoie un statut HTTP 204 (No Content) si la suppression a réussi
    }
}
