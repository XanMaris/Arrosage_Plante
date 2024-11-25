package com.polytech.arrosageplante.plant.sensor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Sensor {
    @Id
    private Long id;
    private String name;

    protected Sensor() {

    }

    public Sensor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
