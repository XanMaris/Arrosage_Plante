#include <Arduino.h>

#ifndef SOIL_MOISTURE_SENSOR_H
#define SOIL_MOISTURE_SENSOR_H

class SoilMoistureSensor
{
    private:
        uint8_t pin;
    public:
        SoilMoistureSensor(uint8_t pin);
        void begin();
        float humidity();
};

#endif