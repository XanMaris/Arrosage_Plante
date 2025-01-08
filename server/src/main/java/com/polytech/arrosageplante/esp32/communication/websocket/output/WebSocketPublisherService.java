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
        commandWebSocketHandler.fillPlant(new OutputCommandFillPlant(plant.getWaterByDayPercentage(), plant.getWaterRetentionCoefficient()), plant.getId().toString());
    }

    public String associatePlantIdToEsp32(String privateCode, String id) {
        return this.commandWebSocketHandler.associatePlantToEsp32(privateCode, id);
    }

    public void disassociatePlantIdToEsp32(Long plantId) {
        this.commandWebSocketHandler.disassociatePlantIdToEsp32(plantId);
    }
}
