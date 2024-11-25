package com.polytech.arrosageplante.plant.service;

import org.springframework.web.multipart.MultipartFile;

public record PlantAddDTO(
        String name,
        String description,
        double waterByDayPercentage,
        double waterRetentionCoefficient,
        long humidityRateSensorId,
        boolean autoWatering,
        MultipartFile image
) {
}
