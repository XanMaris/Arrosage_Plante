package com.polytech.arrosageplante.esp32.communication.websocket.output.command;

import com.polytech.arrosageplante.esp32.communication.websocket.output.OutputAction;

public record OutputCommandFillPlant(double humidityTarget, double soilWaterRetentionFactor) {
    public OutputAction getAction() {
        return OutputAction.ARROSER;
    }
}
