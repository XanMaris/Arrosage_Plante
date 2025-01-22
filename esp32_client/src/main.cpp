#include <Arduino.h>
#include <Wire.h>
#include <sensor/air_sensor.h>
#include <sensor/soil_moisture_sensor.h>
#include <sensor/water_level_sensor.h>
#include <actuator/pump.h>
#include <web_socket/websocket_manager.h>

#define ATTINY1_HIGH_ADDR 0x78
#define ATTINY2_LOW_ADDR 0x77

AirSensor airSensor = AirSensor(A0);
SoilMoistureSensor soilMoistureSensor = SoilMoistureSensor(A2);
WaterLevelSensor waterLevelSensor = WaterLevelSensor(ATTINY1_HIGH_ADDR, ATTINY2_LOW_ADDR); // SDA is i2c pin
Pump pump = Pump(A6);                                                                      // broche d2
WebSocketManager webSocketManager = WebSocketManager(airSensor, soilMoistureSensor, waterLevelSensor, pump);

void websocketTask(void *parameter)
{
    while (true)
    {
        webSocketManager.loop();
        vTaskDelay(10 / portTICK_PERIOD_MS);
    }
}

void sensorTask(void *parameter)
{
    while (true)
    {
        webSocketManager.sendPeriodicSensorData();

        vTaskDelay(1000 / portTICK_PERIOD_MS);
    }
}

void setup()
{
    Serial.begin(9600);

    // Connect to Wi - Fi
    WiFi.begin("mate 10 pro", "azerty123456789");
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }
    Serial.println("\n[Wi-Fi] Connected");
    Serial.print("[Wi-Fi] IP Address: ");
    Serial.println(WiFi.localIP());

    airSensor.begin();
    soilMoistureSensor.begin();
    pump.begin();
    webSocketManager.begin("192.168.43.121", 8080, "/api/ws", WiFi.macAddress());

    xTaskCreatePinnedToCore(
        websocketTask,
        "WebSocket Task",
        4096,
        NULL,
        1,
        NULL,
        1);

    xTaskCreatePinnedToCore(
        sensorTask,
        "Sensor Task",
        4096,
        NULL,
        1,
        NULL,
        1);
    waterLevelSensor.begin();
}

void loop()
{
    vTaskDelay(1000 / portTICK_PERIOD_MS);
}