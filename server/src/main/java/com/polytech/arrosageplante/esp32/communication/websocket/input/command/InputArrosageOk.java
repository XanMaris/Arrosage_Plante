package com.polytech.arrosageplante.esp32.communication.websocket.input.command;

import com.polytech.arrosageplante.esp32.communication.websocket.input.InputAction;

public final class InputArrosageOk extends InputCommand {
    private final String plantId;

    public InputArrosageOk(String plantId) {
        this.plantId = plantId;
    }

    @Override
    public InputAction getAction() {
        return InputAction.ARROSAGE_OK;
    }

    public String getPlantId() {
        return plantId;
    }
}
