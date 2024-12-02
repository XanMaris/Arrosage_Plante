#include "websocket_manager.h"

void WebSocketManager::onMessageReceived(WStype_t type, uint8_t *payload, size_t length)
{
    switch (type) {
        case WStype_DISCONNECTED:
            Serial.println("[WebSocket] Disconnected");
            break;

        case WStype_CONNECTED:
            Serial.println("[WebSocket] Connected");
            this->webSocketClient.sendTXT("Hello from ESP32");
            break;

        case WStype_TEXT: {
            Serial.printf("[WebSocket] Message received: %s\n", payload);

            StaticJsonDocument<256> jsonDoc;
            DeserializationError error = deserializeJson(jsonDoc, payload, length);

            if (error) {
                Serial.print("[JSON] Deserialization failed: ");
                Serial.println(error.c_str());
                return;
            }

            const char* action = jsonDoc["action"];
            if (action == nullptr) {
                Serial.println("[JSON] 'action' field missing");
                return;
            }

            if (strcmp(action, "ARROSER") == 0) {
                this->handleArroser();
            } else if (strcmp(action, "GETHUMIDITY") == 0) {
                this->handleGetHumidity();
            } else if (strcmp(action, "GETWATERLEVEL") == 0) {
                this->handleGetWaterLevel();
            } else {
                Serial.printf("[Action] Unknown action: %s\n", action);
            }
            break;
        }

        default:
            break;
    }
}

WebSocketManager::WebSocketManager(AirSensor& airSensor) : webSocketClient(WebSocketsClient()), airSensor(airSensor)
{
}

void WebSocketManager::begin(const char *host, uint16_t port, const char *endpoint)
{
    this->webSocketClient.begin(host, port, endpoint, "json");
    this->webSocketClient.onEvent([this](WStype_t type, uint8_t* payload, size_t length) {
        this->onMessageReceived(type, payload, length);
    });
}

void WebSocketManager::loop()
{
    this->webSocketClient.loop();
}

void WebSocketManager::handleArroser()
{
    Serial.println("[Action] Handling 'ARROSER'");
}

void WebSocketManager::handleGetHumidity()
{
    float humidityValue = this->airSensor.humidity();
    
    StaticJsonDocument<128> jsonDoc;
    jsonDoc["action"] = "GETHUMIDITY";
    
    char valueStr[6];
    snprintf(valueStr, sizeof(valueStr), "%.2f", humidityValue);
    jsonDoc["value"] = valueStr;

    char buffer[128];
    serializeJson(jsonDoc, buffer, sizeof(buffer));

    Serial.println(humidityValue);

    this->webSocketClient.sendTXT(buffer);
}

void WebSocketManager::handleGetWaterLevel()
{
}
