package com.polytech.arrosageplante.esp32.communication.websocket.output;

import com.polytech.arrosageplante.esp32.communication.websocket.CommandWebSocketHandler;
import com.polytech.arrosageplante.esp32.communication.websocket.output.command.OutputCommandFillPlant;
import com.polytech.arrosageplante.plant.domain.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPublisherService {

    @Autowired
    private CommandWebSocketHandler commandWebSocketHandler;

    public void arroser(Plant plant) {
        commandWebSocketHandler.fillPlant(new OutputCommandFillPlant(plant.getWaterByDayPercentage(), plant.getWaterRetentionCoefficient()));
    }

    public void associatePlantIdToEsp32(String id) {
        this.commandWebSocketHandler.associatePlantToEsp32(id);
    }
}
