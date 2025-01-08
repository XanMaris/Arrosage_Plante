package com.polytech.arrosageplante.plant.measure.domain;

import com.polytech.arrosageplante.plant.domain.Plant;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class PlantMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreationTimestamp
    private LocalDateTime measureDate;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    private double airHumidity;
    private double airTemperature;
    private double soilHumidity;

    public PlantMeasure(Plant plant, double airHumidity, double airTemperature, double soilHumidity) {
        this.plant = plant;
        this.airHumidity = airHumidity;
        this.airTemperature = airTemperature;
        this.soilHumidity = soilHumidity;
    }

    protected PlantMeasure() {

    }

    public String getId() {
        return id;
    }

    public LocalDateTime getMeasureDate() {
        return measureDate;
    }

    public Plant getPlant() {
        return plant;
    }

    public double getAirHumidity() {
        return airHumidity;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    public double getSoilHumidity() {
        return soilHumidity;
    }
}
