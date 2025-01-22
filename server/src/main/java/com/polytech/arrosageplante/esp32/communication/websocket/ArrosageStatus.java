package com.polytech.arrosageplante.esp32.communication.websocket;

public record ArrosageStatus(boolean hasReceivedResponse, boolean success) {
}
