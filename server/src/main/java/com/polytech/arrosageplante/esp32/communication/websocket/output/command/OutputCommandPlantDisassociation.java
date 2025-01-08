package com.polytech.arrosageplante.esp32.communication.websocket.output.command;

import com.polytech.arrosageplante.esp32.communication.websocket.output.OutputAction;

public record OutputCommandPlantDisassociation(String plantId) {
    public OutputAction getAction() {
        return OutputAction.PLANTDISASSOCIATION;
    }
}
