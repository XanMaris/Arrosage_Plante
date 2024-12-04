package com.polytech.arrosageplante.plant.service;

import org.springframework.web.multipart.MultipartFile;

public record PlantAddDTO(
        String name,
        String description,
        double waterByDayPercentage,
        double waterRetentionCoefficient,
        boolean autoWatering,
        MultipartFile image,
        String privateEsp32Code
) {
}
