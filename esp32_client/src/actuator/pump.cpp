#include "Pump.h"

Pump::Pump(uint8_t pin): pin(pin)
{
}

void Pump::begin()
{
    pinMode(this->pin, OUTPUT);
    digitalWrite(this->pin, LOW); 
}

bool Pump::fill(float humidityInitial, float currentWaterLevel, float humidityTarget, float soilWaterRetentionFactor)
{
    float humidityDifference = humidityTarget - humidityInitial;

    if (humidityDifference <= 0) {
        Serial.println("Humidité cible déjà atteinte, pas besoin d'ajouter d'eau.");
        return true;
    }

    float waterNeeded = (humidityDifference / soilWaterRetentionFactor) * this->recipientVolume;

    if(waterNeeded >= currentWaterLevel) {
        Serial.println("Pas assez d'eau dans le récipient");
        return false;
    }

    unsigned long pumpDuration = waterNeeded / this->waterFlowRate * 1000;

    Serial.println(pumpDuration);
    Serial.println(this->pin);

    digitalWrite(this->pin, HIGH);
    delay(pumpDuration);
    digitalWrite(this->pin, LOW); 

    Serial.println("Arrosage terminé.");
    return true;
}
