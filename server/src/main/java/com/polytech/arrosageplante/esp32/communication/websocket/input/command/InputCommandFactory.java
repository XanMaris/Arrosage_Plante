package com.polytech.arrosageplante.esp32.communication.websocket.input.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.arrosageplante.esp32.communication.websocket.input.InputAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputCommandFactory {
    private final ObjectMapper objectMapper;

    @Autowired
    public InputCommandFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public InputCommand createCommand(String json) throws JsonProcessingException {
        JsonNode node = this.objectMapper.readTree(json);
        InputAction action = InputAction.valueOf(node.get("action").asText());

        return switch (action) {
            case PLANT_DETAILS -> objectMapper.readValue(json, InputPlantDetailsCommand.class);
            case SYNC -> objectMapper.readValue(json, InputSyncCommand.class);
            case ARROSAGE_NOT_ENOUGH_WATER -> objectMapper.readValue(json, InputArrosageKo.class);
            case ARROSAGE_OK -> objectMapper.readValue(json, InputArrosageOk.class);
        };
    }
}
