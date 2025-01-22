#include <Arduino.h>

#ifndef PUMP_H
#define PUMP_H

class Pump
{
private:
    uint8_t pin;
    /**
     * Taille en Litre
     */
    float recipientVolume = 1.0;
    /**
     * Débit de la pompe en litre/s
     */
    float waterFlowRate = 0.077;
public:
    Pump(uint8_t pin);
    void begin();
    bool fill(float humidityInitial, float currentWaterLevel, float humidityTarget, float soilWaterRetentionFactor);
};
#endif