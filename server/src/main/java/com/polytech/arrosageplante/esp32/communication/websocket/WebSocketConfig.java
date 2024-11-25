package com.polytech.arrosageplante.esp32.communication.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CommandWebSocketHandler commandWebSocketHandler;

    public WebSocketConfig(CommandWebSocketHandler commandWebSocketHandler) {
        this.commandWebSocketHandler = commandWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(commandWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}