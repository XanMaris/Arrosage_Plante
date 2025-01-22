#ifndef WATER_LEVEL_SENSOR_H
#define WATER_LEVEL_SENSOR_H

#include <Arduino.h>
#include <Wire.h>

class WaterLevelSensor {
public:
    /**
     * @brief Constructeur de la classe WaterLevelSensor
     * 
     * @param highAddr Adresse I2C pour les 12 sections hautes (0x78)
     * @param lowAddr Adresse I2C pour les 8 sections basses (0x77)
     */
    WaterLevelSensor(uint8_t highAddr, uint8_t lowAddr);

    /**
     * @brief Initialise la communication I2C
     * 
     */
    void begin();

    /**
     * @brief Lit le niveau d'eau en litres
     * 
     * @return float Niveau d'eau en litres
     */
    float waterLevel();

private:
    uint8_t _highAddr;          // Adresse I2C pour les 12 sections hautes
    uint8_t _lowAddr;           // Adresse I2C pour les 8 sections basses
    unsigned char low_data[8];  // Données reçues pour les 8 sections basses
    unsigned char high_data[12];// Données reçues pour les 12 sections hautes

    // Constantes de calibration
    const float minAnalogValue = 0.0;   
    const float maxAnalogValue = 1023.0;
    const float maxVolumeLiters = 1.0;
    
    /**
     * @brief Lit les données des sections hautes depuis l'adresse I2C
     */
    void getHigh12SectionValue();

    /**
     * @brief Lit les données des sections basses depuis l'adresse I2C
     */
    void getLow8SectionValue();
};

#endif // WATER_LEVEL_SENSOR_H