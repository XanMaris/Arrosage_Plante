package com.polytech.arrosageplante.esp32.communication.websocket.input;

import org.springframework.web.socket.WebSocketSession;

public record WebSocketUnsynchronized(WebSocketSession webSocketSession, String syncCode) {
}
