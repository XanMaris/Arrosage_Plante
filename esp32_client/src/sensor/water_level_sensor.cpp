#include "water_level_sensor.h"

// Définition des seuils
#define NO_TOUCH       0xFE
#define THRESHOLD      100

// Constructeur de la classe WaterLevelSensor
WaterLevelSensor::WaterLevelSensor(uint8_t highAddr, uint8_t lowAddr)
    : _highAddr(highAddr), _lowAddr(lowAddr)
{
    // Initialisation des tableaux de données
    memset(low_data, 0, sizeof(low_data));
    memset(high_data, 0, sizeof(high_data));
}

void WaterLevelSensor::begin()
{
    Wire.begin();
}

// Lit les données des 12 sections hautes depuis l'adresse I2C spécifiée
void WaterLevelSensor::getHigh12SectionValue()
{
    memset(high_data, 0, sizeof(high_data));
    Wire.requestFrom(_highAddr, (uint8_t)12); // Demande 12 octets

    // Attendre que les données soient disponibles
    unsigned long startTime = millis();
    while (Wire.available() < 12) {
        if (millis() - startTime > 1000) { // Timeout après 1 seconde
            Serial.println("Timeout: Impossible de lire les sections hautes.");
            break;
        }
    }

    // Lire les données reçues
    for (int i = 0; i < 12 && Wire.available(); i++) {
        high_data[i] = Wire.read();
    }
    delay(10); // Bref délai pour stabiliser la lecture
}

// Lit les données des 8 sections basses depuis l'adresse I2C spécifiée
void WaterLevelSensor::getLow8SectionValue()
{
    memset(low_data, 0, sizeof(low_data));
    Wire.requestFrom(_lowAddr, (uint8_t)8); // Demande 8 octets

    // Attendre que les données soient disponibles
    unsigned long startTime = millis();
    while (Wire.available() < 8) {
        if (millis() - startTime > 1000) { // Timeout après 1 seconde
            Serial.println("Timeout: Impossible de lire les sections basses.");
            break;
        }
    }

    // Lire les données reçues
    for (int i = 0; i < 8 && Wire.available(); i++) {
        low_data[i] = Wire.read();
    }
    delay(10); // Bref délai pour stabiliser la lecture
}

float WaterLevelSensor::waterLevel()
{
    const int sensorvalue_min = 250;
    const int sensorvalue_max = 255;
    int low_count = 0;
    int high_count = 0;

    getLow8SectionValue();
    getHigh12SectionValue();

    // Calculer la valeur de touche en fonction des seuils
    uint32_t touch_val = 0;
    uint8_t trig_section = 0;

    for (int i = 0 ; i < 8; i++) {
        if (low_data[i] > THRESHOLD) {
            touch_val |= 1 << i;
        }
    }
    for (int i = 0 ; i < 12; i++) {
        if (high_data[i] > THRESHOLD) {
            touch_val |= (uint32_t)1 << (8 + i);
        }
    }

    // Calculer le nombre de sections déclenchées
    while (touch_val & 0x01) {
        trig_section++;
        touch_val >>= 1;
    }

    // Calculer le niveau d'eau en pourcentage
    float percentage = trig_section * 5.0; // Chaque section représente 5%

    // Convertir le pourcentage en litres
    float waterLevelLiters = (percentage / 100.0) * maxVolumeLiters;

    return waterLevelLiters;
}