package com.polytech.arrosageplante.esp32.communication.websocket.exception;

public class CommunicationError extends RuntimeException {
    public CommunicationError() {
        super("Erreur de communication avec l'ESP32");
    }

    public CommunicationError(String message) {
        super(message);
    }
}
