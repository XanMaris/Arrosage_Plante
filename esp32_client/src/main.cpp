#include <Arduino.h>
#include <sensor/air_sensor.h>
#include <sensor/soil_moisture_sensor.h>
#include <web_socket/websocket_manager.h>

AirSensor airSensor = AirSensor(A0);
SoilMoistureSensor soilMoistureSensor = SoilMoistureSensor(A2);
WebSocketManager webSocketManager = WebSocketManager(airSensor, soilMoistureSensor);

void setup() {
  Serial.begin(9600);

  // Connect to Wi-Fi
  WiFi.begin("mate 10 pro", "azerty123456789");
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
  }
  Serial.println("\n[Wi-Fi] Connected");
  Serial.print("[Wi-Fi] IP Address: ");
  Serial.println(WiFi.localIP());

  airSensor.begin();
  soilMoistureSensor.begin();
  webSocketManager.begin("192.168.43.121", 8080, "/api/ws");
}

void loop() {
  webSocketManager.loop();
}