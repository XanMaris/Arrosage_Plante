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

            if(strcmp(action, "PLANTASSOCIATION") == 0) {
                const char* plantId = jsonDoc["plantId"];
                this->plantId = plantId;
            } else {
                if(this->plantId == nullptr) {
                    Serial.printf("Error during plant association");
                }

                if (strcmp(action, "ARROSER") == 0) {
                    const float humidityTarget = jsonDoc["humidityTarget"];
                    const float soilWaterRetentionFactor = jsonDoc["soilWaterRetentionFactor"];
                    this->handleArroser(humidityTarget, soilWaterRetentionFactor);
                } else if (strcmp(action, "GETHUMIDITY") == 0) {
                    this->handleGetAirHumidity();
                } else if (strcmp(action, "GETWATERLEVEL") == 0) {
                    this->handleGetWaterLevel();
                } else {
                    Serial.printf("[Action] Unknown action: %s\n", action);
                }
            }
            break;
        }

        default:
            break;
    }
}

WebSocketManager::WebSocketManager(AirSensor& airSensor, SoilMoistureSensor& soilMoistureSensor, Pump& pump) : webSocketClient(WebSocketsClient()), airSensor(airSensor), soilMoistureSensor(soilMoistureSensor), pump(pump)
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
    this->sendPeriodicSensorData();
}

void WebSocketManager::handleArroser(float humidityTarget, float soilWaterRetentionFactor)
{
    Serial.println("[Action] Handling 'ARROSER'");
    this->pump.fill(this->soilMoistureSensor.humidity(), humidityTarget, soilWaterRetentionFactor);
}

void WebSocketManager::handleGetAirHumidity()
{
    float humidityValue = this->airSensor.humidity();
    
    StaticJsonDocument<128> jsonDoc;
    jsonDoc["action"] = "GETHUMIDITY";
    
    char valueStr[6];
    snprintf(valueStr, sizeof(valueStr), "%.2f", humidityValue);
    jsonDoc["value"] = valueStr;

    jsonDoc["plantId"] = plantId;

    char buffer[128];
    serializeJson(jsonDoc, buffer, sizeof(buffer));

    Serial.println(humidityValue);

    this->webSocketClient.sendTXT(buffer);
}

void WebSocketManager::handleGetWaterLevel()
{
}

void WebSocketManager::sendPeriodicSensorData()
{
    unsigned long currentMillis = millis();
    if (currentMillis - lastSensorSendTime >= sensorSendInterval) {
        lastSensorSendTime = currentMillis;

        float airHumidity = this->airSensor.humidity();
        float airTemperature = this->airSensor.temperature();
        float soilHumidity = this->soilMoistureSensor.humidity();

        StaticJsonDocument<256> jsonDoc;
        jsonDoc["action"] = "PLANT_DETAILS";
        char airHumidityStr[6];
        snprintf(airHumidityStr, sizeof(airHumidityStr), "%.2f", airHumidity);
        jsonDoc["airHumidity"] = airHumidityStr;
        char airTemperatureStr[6];
        snprintf(airTemperatureStr, sizeof(airTemperatureStr), "%.2f", airTemperature);
        jsonDoc["airTemperature"] = airTemperatureStr;
        char soilHumidityStr[6];
        snprintf(soilHumidityStr, sizeof(soilHumidityStr), "%.2f", soilHumidity);
        jsonDoc["soilHumidity"] = soilHumidityStr;
        jsonDoc["plantId"] = this->plantId;

        String json;
        serializeJson(jsonDoc, json);
        this->webSocketClient.sendTXT(json);

        Serial.printf("[Sensor Data] airHumidity: %.2f, airTemperature: %.2f\n", airHumidity, airTemperature);
    }
}