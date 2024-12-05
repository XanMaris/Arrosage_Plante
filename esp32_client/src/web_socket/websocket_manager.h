#include <WebSocketsClient.h>
#include <ArduinoJson.h>
#include <sensor/air_sensor.h>

class WebSocketManager
{
private:
    WebSocketsClient webSocketClient;
    AirSensor& airSensor;
    void onMessageReceived(WStype_t type, uint8_t* payload, size_t length);
public:
    WebSocketManager(AirSensor& airSensor);

    void begin(const char* host, uint16_t port, const char* endpoint);
    void loop();
    void handleArroser();
    void handleGetHumidity();
    void handleGetWaterLevel();
};