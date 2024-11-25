package com.polytech.arrosageplante.plant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public final class Plant {
    @Id
    private Long id;
    private String name;
    private String description;
    private double humidityRate;
    private boolean automaticWatering;

    public Plant(String name, String description, double humidityRate, boolean automaticWatering) {
        this.name = name;
        this.description = description;
        this.humidityRate = humidityRate;
        this.automaticWatering = automaticWatering;
    }

    protected Plant() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getHumidityRate() {
        return humidityRate;
    }

    public boolean getAutomaticWatering() {
        return automaticWatering;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Plant) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Double.doubleToLongBits(this.humidityRate) == Double.doubleToLongBits(that.humidityRate) &&
                this.automaticWatering == that.automaticWatering;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, humidityRate, automaticWatering);
    }

    @Override
    public String toString() {
        return "Plant[" +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "humidityRate=" + humidityRate + ", " +
                "automaticWatering=" + automaticWatering + ']';
    }

}
