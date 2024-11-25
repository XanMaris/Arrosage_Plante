package com.polytech.arrosageplante.plant.controller;

import com.polytech.arrosageplante.plant.domain.Plant;

public record PlantDetailDto(
        String id,
        String name,
        String description,
        double humidityRate,
        double waterRetentionCoefficient,
        boolean automaticWatering,
        double waterByDayPercentage
) {
    public static PlantDetailDto fromPlant(Plant plant, double humidityRate) {
        return new PlantDetailDto(plant.getId(),
                plant.getName(),
                plant.getDescription(),
                humidityRate,
                plant.getWaterRetentionCoefficient(),
                plant.getAutomaticWatering(),
                plant.getWaterByDayPercentage());
    }
}
