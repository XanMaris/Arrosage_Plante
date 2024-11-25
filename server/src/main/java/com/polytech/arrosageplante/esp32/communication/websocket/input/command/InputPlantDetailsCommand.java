package com.polytech.arrosageplante.esp32.communication.websocket.input.command;

import com.polytech.arrosageplante.esp32.communication.websocket.input.InputAction;

public final class InputPlantDetailsCommand extends InputCommand {
    private final double airHumidity;
    private final double airTemperature;
    private final double soilHumidity;
    private final String plantId;

    public InputPlantDetailsCommand(double airHumidity, double airTemperature, double soilHumidity, String plantId) {
        this.airHumidity = airHumidity;
        this.airTemperature = airTemperature;
        this.soilHumidity = soilHumidity;
        this.plantId = plantId;
    }

    @Override
    public InputAction getAction() {
        return InputAction.PLANT_DETAILS;
    }

    public double getAirHumidity() {
        return airHumidity;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public String getPlantId() {
        return this.plantId;
    }

    public double getSoilHumidity() {
        return soilHumidity;
    }
}
