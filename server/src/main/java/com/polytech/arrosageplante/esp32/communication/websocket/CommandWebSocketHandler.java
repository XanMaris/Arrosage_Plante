package com.polytech.arrosageplante.esp32.communication.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputCommand;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputCommandFactory;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputPlantDetailsCommand;
import com.polytech.arrosageplante.esp32.communication.websocket.input.handler.PlantDetailsReceiveService;
import com.polytech.arrosageplante.esp32.communication.websocket.output.command.OutputCommand;
import com.polytech.arrosageplante.esp32.communication.websocket.output.command.OutputCommandPlantAssociation;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.measure.domain.PlantMeasure;
import com.polytech.arrosageplante.plant.measure.service.PlantMeasureService;
import com.polytech.arrosageplante.plant.service.PlantCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CommandWebSocketHandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;
    private final InputCommandFactory inputCommandFactory;
    private final PlantDetailsReceiveService plantDetailsReceiveService;

    @Autowired
    public CommandWebSocketHandler(ObjectMapper objectMapper,
                                   InputCommandFactory inputCommandFactory,
                                   PlantDetailsReceiveService plantDetailsReceiveService) {
        this.objectMapper = objectMapper;
        this.inputCommandFactory = inputCommandFactory;
        this.plantDetailsReceiveService = plantDetailsReceiveService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("ESP32 connecté: " + session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Message reçu de ESP32: " + message.getPayload());

        try {
            InputCommand command = this.inputCommandFactory.createCommand(message.getPayload());
            switch (command) {
                case InputPlantDetailsCommand inputPlantDetailsCommand -> this.plantDetailsReceiveService.handleReceive(inputPlantDetailsCommand);
            }
        } catch (Exception e) {
            System.out.println("Message non géré");

        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Erreur de transport: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
        sessions.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connexion fermée: " + session.getId());
        sessions.remove(session);
    }

    private void sendMessage(TextMessage textMessage) {
        try {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(OutputCommand outputCommand) {
        try {
            String jsonCommand = this.objectMapper.writeValueAsString(outputCommand);
            this.sendMessage(new TextMessage(jsonCommand));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void associatePlantToEsp32(String plantId) {
        try {
            String jsonCommand = this.objectMapper.writeValueAsString(new OutputCommandPlantAssociation(plantId));
            this.sendMessage(new TextMessage(jsonCommand));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
