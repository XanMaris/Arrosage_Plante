#include "air_sensor.h"

AirSensor::AirSensor(uint8_t pin): dht(DHT(pin, DHT22))
{
}

void AirSensor::begin()
{
    this->dht.begin();
}

float AirSensor::humidity()
{
    return this->dht.readHumidity();
}

float AirSensor::temperature()
{
    return this->dht.readTemperature(false);
}
