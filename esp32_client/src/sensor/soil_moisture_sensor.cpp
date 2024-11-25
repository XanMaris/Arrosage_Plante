#include "soil_moisture_sensor.h"

SoilMoistureSensor::SoilMoistureSensor(uint8_t pin): pin(pin)
{
}

void SoilMoistureSensor::begin()
{
    pinMode(this->pin, INPUT);
}

float SoilMoistureSensor::humidity()
{
    float bruteValue = analogRead(this->pin);

    float humidityPercentage = map(bruteValue, 0, 4095, 0, 100);

    return humidityPercentage;
}
