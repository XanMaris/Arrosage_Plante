#include <stdint.h>
#include <Arduino.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>

#ifndef AIR_SENSOR_H
#define AIR_SENSOR_H

class AirSensor
{
    private:
        DHT dht;
    public:
        AirSensor(uint8_t pin);
        void begin();
        float humidity();
        float temperature();
};

#endif