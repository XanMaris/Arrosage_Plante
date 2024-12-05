package com.polytech.arrosageplante.esp32.communication.websocket.exception;

public class FirstSynchronizationFailedException extends RuntimeException {
    public FirstSynchronizationFailedException() {
        super("Erreur lors de l'association avec l'ESP32");
    }
}
