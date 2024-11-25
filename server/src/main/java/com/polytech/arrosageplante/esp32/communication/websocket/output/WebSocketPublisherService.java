package com.polytech.arrosageplante.esp32.communication.websocket.output;

import com.polytech.arrosageplante.esp32.communication.websocket.CommandWebSocketHandler;
import com.polytech.arrosageplante.esp32.communication.websocket.output.command.OutputCommand;
import com.polytech.arrosageplante.plant.domain.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPublisherService {

    @Autowired
    private CommandWebSocketHandler commandWebSocketHandler;

    public void sendCommand(OutputCommand outputCommand) {
        commandWebSocketHandler.sendCommand(outputCommand);
    }

    public void arroser(Plant plant) {
        sendCommand(new OutputCommand(OutputAction.ARROSER));
    }

    public void associatePlantIdToEsp32(String id) {
        this.commandWebSocketHandler.associatePlantToEsp32(id);
    }
}
