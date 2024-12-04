package com.polytech.arrosageplante.esp32.communication.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.arrosageplante.esp32.communication.websocket.exception.CommunicationError;
import com.polytech.arrosageplante.esp32.communication.websocket.exception.FirstSynchronizationFailedException;
import com.polytech.arrosageplante.esp32.communication.websocket.input.WebSocketUnsynchronized;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputCommand;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputCommandFactory;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputPlantDetailsCommand;
import com.polytech.arrosageplante.esp32.communication.websocket.input.command.InputSyncCommand;
import com.polytech.arrosageplante.esp32.communication.websocket.input.handler.PlantDetailsReceiveService;
import com.polytech.arrosageplante.esp32.communication.websocket.output.command.OutputCommandFillPlant;
import com.polytech.arrosageplante.esp32.communication.websocket.output.command.OutputCommandPlantAssociation;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandWebSocketHandler extends TextWebSocketHandler {
    /**
     * Session entre un ESP32 et le serveur
     * La clé est l'ID de la plante que gère l'ESP32 en question
     */
    private final ConcurrentHashMap<String, WebSocketSession> trustedEsp32SessionHashMap = new ConcurrentHashMap<>();
    /**
     * File d'attente des Websocket qui doivent être synchronisés
     */
    private final List<WebSocketUnsynchronized> unassociatedEsp32 = new ArrayList<>();
    private final ObjectMapper objectMapper;
    private final InputCommandFactory inputCommandFactory;
    private final PlantDetailsReceiveService plantDetailsReceiveService;
    private final PlantRepository plantRepository;

    @Autowired
    public CommandWebSocketHandler(ObjectMapper objectMapper,
                                   InputCommandFactory inputCommandFactory,
                                   PlantDetailsReceiveService plantDetailsReceiveService,
                                   PlantRepository plantRepository) {
        this.objectMapper = objectMapper;
        this.inputCommandFactory = inputCommandFactory;
        this.plantDetailsReceiveService = plantDetailsReceiveService;
        this.plantRepository = plantRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("ESP32 connecté: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Message reçu de ESP32: " + message.getPayload());

        try {
            InputCommand command = this.inputCommandFactory.createCommand(message.getPayload());

            switch (command) {
                case InputPlantDetailsCommand inputPlantDetailsCommand ->
                        this.plantDetailsReceiveService.handleReceive(inputPlantDetailsCommand);
                case InputSyncCommand inputSyncCommand -> {
                    String esp32PrivateCode = inputSyncCommand.getPrivateCode();
                    Plant plant = this.plantRepository.findPlantByEsp32PrivateCode(esp32PrivateCode).orElse(null);
                    if(plant == null) {
                        this.unassociatedEsp32.add(new WebSocketUnsynchronized(session, esp32PrivateCode));
                    } else {
                        this.ackSync(session, plant.getId().toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Message non géré");

        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Erreur de transport: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connexion fermée: " + session.getId());
        unassociatedEsp32.removeIf(webSocketUnsynchronized -> webSocketUnsynchronized.webSocketSession().equals(session));
    }

    public void sendMessage(TextMessage textMessage, String plantId) {
        try {
            this.trustedEsp32SessionHashMap.get(plantId).sendMessage(textMessage);
        } catch (Exception e) {
            throw new CommunicationError();
        }
    }

    private void ackSync(WebSocketSession session, String plantId) {
        try {
            this.trustedEsp32SessionHashMap.put(plantId, session);
            String jsonCommand = this.objectMapper.writeValueAsString(new OutputCommandPlantAssociation(plantId));
            this.sendMessage(new TextMessage(jsonCommand), plantId);
        } catch (Exception e) {
            throw new FirstSynchronizationFailedException();
        }
    }

    /**
     * Envoie la commande d'arrosage à l'esp32
     *
     * @param outputCommandFillPlant
     */
    public void fillPlant(OutputCommandFillPlant outputCommandFillPlant, String plantId) {
        try {
            String jsonCommand = this.objectMapper.writeValueAsString(outputCommandFillPlant);
            this.sendMessage(new TextMessage(jsonCommand), plantId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String associatePlantToEsp32(String privateCode, String plantId) {
        // Vérification si pendant l'ajout de la plante, on a bien lancé le challenge de synchronisation
        WebSocketUnsynchronized webSocketUnsynchronizedFinded = this.unassociatedEsp32.stream().filter(webSocketUnsynchronized -> webSocketUnsynchronized.syncCode().equals(privateCode)).findFirst().orElseThrow(FirstSynchronizationFailedException::new);
        this.unassociatedEsp32.remove(webSocketUnsynchronizedFinded);
        this.ackSync(webSocketUnsynchronizedFinded.webSocketSession(), plantId);

        return webSocketUnsynchronizedFinded.syncCode();
    }
}
