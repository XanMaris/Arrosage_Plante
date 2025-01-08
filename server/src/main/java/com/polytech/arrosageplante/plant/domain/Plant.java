package com.polytech.arrosageplante.plant.domain;

import com.polytech.arrosageplante.plant.measure.domain.PlantMeasure;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public final class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private boolean automaticWatering;
    /**
     * <a href="https://fr.wikipedia.org/wiki/Capacit%C3%A9_au_champ">Coefficient de rétention d'eau du sol</a>
     */
    private double waterRetentionCoefficient;

    private double waterByDayPercentage;

    @Lob
    private byte[] image;
    private String imageContentType;
    @Column(name = "ESP32_PRIVATE_CODE")
    private String esp32PrivateCode = null;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlantMeasure> plantMeasures = new ArrayList<>();

    public Plant(String name,
                 String description,
                 boolean automaticWatering,
                 double waterByDayPercentage,
                 byte[] image,
                 double waterRetentionCoefficient, String imageContentType) {
        this.name = name;
        this.description = description;
        this.automaticWatering = automaticWatering;
        this.waterRetentionCoefficient = waterRetentionCoefficient;
        this.waterByDayPercentage = waterByDayPercentage;
        this.image = image;
        this.imageContentType = imageContentType;
    }

    protected Plant() {

    }

    public byte[] getImage() {
        return this.image;
    }

    public String getImageContentType() {
        return this.imageContentType;
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


    public boolean getAutomaticWatering() {
        return automaticWatering;
    }

    public double getWaterRetentionCoefficient() {
        return this.waterRetentionCoefficient;
    }

    public double getWaterByDayPercentage() { return waterByDayPercentage;}

    public String getEsp32PrivateCode() {
        return esp32PrivateCode;
    }

    public void setEsp32PrivateCode(String esp32PrivateCode) {
        this.esp32PrivateCode = esp32PrivateCode;
    }

    public void setWaterByDayPercentage(double waterByDayPercentage) {
        this.waterByDayPercentage = waterByDayPercentage;
    }

    public void setAutomaticWatering(boolean automaticWatering) {
        this.automaticWatering = automaticWatering;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Plant) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Double.doubleToLongBits(this.waterByDayPercentage) == Double.doubleToLongBits(that.waterByDayPercentage) &&
                Double.doubleToLongBits(this.waterRetentionCoefficient) == Double.doubleToLongBits(that.waterRetentionCoefficient) &&
                Arrays.equals(this.image, that.image) &&
                Objects.equals(this.imageContentType, that.imageContentType) &&
                this.automaticWatering == that.automaticWatering;

    }

    @Override
    public int hashCode() {
        return Objects.hash(name,
                description,
                automaticWatering,
                waterRetentionCoefficient,
                waterByDayPercentage,
                this.imageContentType,
                Arrays.hashCode(image));
    }

    @Override
    public String toString() {
        return "Plant[" +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "automaticWatering=" + automaticWatering +
                "waterByDayPurcentage=" + waterByDayPercentage +
                "waterRetentionCoefficient=" + waterRetentionCoefficient + "]";
    }

}
