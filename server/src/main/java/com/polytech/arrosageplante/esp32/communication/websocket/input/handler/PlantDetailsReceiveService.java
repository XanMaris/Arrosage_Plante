package com.polytech.arrosageplante.esp32.communication.websocket.input.handler;

import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputPlantDetailsCommand;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.measure.domain.PlantMeasure;
import com.polytech.arrosageplante.plant.measure.service.PlantMeasureService;
import com.polytech.arrosageplante.plant.repository.PlantRepository;
import org.springframework.stereotype.Service;

@Service
public class PlantDetailsReceiveService {
    private final PlantRepository plantRepository;
    private final PlantMeasureService plantMeasureService;

    public PlantDetailsReceiveService(PlantRepository plantRepository, PlantMeasureService plantMeasureService) {
        this.plantRepository = plantRepository;
        this.plantMeasureService = plantMeasureService;
    }

    public void handleReceive(InputPlantDetailsCommand inputPlantDetailsCommand) {
        Plant plant = this.plantRepository.findById(inputPlantDetailsCommand.getPlantId()).orElseThrow(() -> new RuntimeException("Plante inconnue"));
        this.plantMeasureService.saveMeasure(new PlantMeasure(plant,
                inputPlantDetailsCommand.getAirHumidity(),
                inputPlantDetailsCommand.getAirTemperature(),
                inputPlantDetailsCommand.getSoilHumidity()
        ));
    }
}
