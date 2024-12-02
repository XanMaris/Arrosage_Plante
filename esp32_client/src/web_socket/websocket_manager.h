#include <WebSocketsClient.h>
#include <ArduinoJson.h>
#include <sensor/air_sensor.h>
#include <sensor/soil_moisture_sensor.h>
#include <actuator/pump.h>

class WebSocketManager
{
private:
    WebSocketsClient webSocketClient;
    AirSensor& airSensor;
    SoilMoistureSensor& soilMoistureSensor;
    Pump& pump;
    void onMessageReceived(WStype_t type, uint8_t* payload, size_t length);

    const char* plantId = "1"; // TODO: for test only

    unsigned long lastSensorSendTime = 0;
    const unsigned long sensorSendInterval = 10000; // 60 secondes
public:
    WebSocketManager(AirSensor& airSensor, SoilMoistureSensor& soilMoistureSensor, Pump& pump);

    void begin(const char* host, uint16_t port, const char* endpoint);
    void loop();
    void handleArroser(float humidityTarget, float soilWaterRetentionFactor);
    void handleGetAirHumidity();
    void handleGetWaterLevel();
    void sendPeriodicSensorData();
};