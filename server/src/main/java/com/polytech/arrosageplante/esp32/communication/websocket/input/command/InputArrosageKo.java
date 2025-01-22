package com.polytech.arrosageplante.esp32.communication.websocket.input.command;

import com.polytech.arrosageplante.esp32.communication.websocket.input.InputAction;

public final class InputArrosageKo extends InputCommand {
    private final String plantId;

    public InputArrosageKo(String plantId) {
        this.plantId = plantId;
    }

    @Override
    public InputAction getAction() {
        return InputAction.ARROSAGE_NOT_ENOUGH_WATER;
    }

    public String getPlantId() {
        return plantId;
    }
}
