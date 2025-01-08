#include "websocket_manager.h"

void WebSocketManager::onMessageReceived(WStype_t type, uint8_t *payload, size_t length)
{
    switch (type) {
        case WStype_DISCONNECTED:
            Serial.println("[WebSocket] Disconnected");
            this->isAssociatedToPlant = false;
            this->plantId = "";
            break;

        case WStype_CONNECTED:
            {
                Serial.println("[WebSocket] Connected");
                this->preSyncToServer();
            }
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
                this->plantId = String(jsonDoc["plantId"].as<const char*>());
                this->isAssociatedToPlant = true;
            }
            else if(strcmp(action, "PLANTDISASSOCIATION") == 0) {
                this->isAssociatedToPlant = false;
                this->plantId = "";
                this->reSyncToServer();
            }
            else {
                if(this->plantId == nullptr || this->plantId == "") {
                    Serial.printf("Error during plant association");
                }
                else if (strcmp(action, "ARROSER") == 0) {
                    const float humidityTarget = jsonDoc["humidityTarget"];
                    const float soilWaterRetentionFactor = jsonDoc["soilWaterRetentionFactor"];
                    this->handleArroser(humidityTarget, soilWaterRetentionFactor);
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

void WebSocketManager::begin(const char *host, uint16_t port, const char *endpoint, String macAdress)
{
    this->webSocketClient.begin(host, port, endpoint, "json");
    this->webSocketClient.onEvent([this](WStype_t type, uint8_t* payload, size_t length) {
        this->onMessageReceived(type, payload, length);
    });
}

bool WebSocketManager::preSyncToServer() 
{
    StaticJsonDocument<128> jsonDoc;
    
    jsonDoc["action"] = "SYNC";
    jsonDoc["privateCode"] = "123456789"; // TODO: use macAdress/unique code when finished
    char buffer[128];
    serializeJson(jsonDoc, buffer, sizeof(buffer));
    return this->webSocketClient.sendTXT(buffer);
}

void WebSocketManager::reSyncToServer()
{
    this->preSyncToServer();
}

void WebSocketManager::loop()
{
    this->webSocketClient.loop();
}

void WebSocketManager::handleArroser(float humidityTarget, float soilWaterRetentionFactor)
{
    Serial.println("[Action] Handling 'ARROSER'");
    this->pump.fill(this->soilMoistureSensor.humidity(), humidityTarget, soilWaterRetentionFactor);
}

void WebSocketManager::sendPeriodicSensorData()
{
    if(this->isAssociatedToPlant == false) {
        return;
    }

    float airHumidity = airSensor.humidity();
    float airTemperature = airSensor.temperature();
    float soilHumidity = soilMoistureSensor.humidity();

    StaticJsonDocument<256> jsonDoc;
    jsonDoc["action"] = "PLANT_DETAILS";
    char airHumidityStr[6];
    snprintf(airHumidityStr, sizeof(airHumidityStr), "%.2f", airHumidity);
    jsonDoc["airHumidity"] = airHumidityStr;
    char airTemperatureStr[6];
    snprintf(airTemperatureStr, sizeof(airTemperatureStr), "%.2f", airTemperature);
    jsonDoc["airTemperature"] = airTemperatureStr;
    char soilHumidityStr[6];
    jsonDoc["soilHumidity"] = soilHumidityStr;
    jsonDoc["plantId"] = this->plantId;

    String json;
    serializeJson(jsonDoc, json);
    this->webSocketClient.sendTXT(json);

    Serial.printf("[Sensor Data] airHumidity: %.2f, airTemperature: %.2f\n", airHumidity, airTemperature);
}