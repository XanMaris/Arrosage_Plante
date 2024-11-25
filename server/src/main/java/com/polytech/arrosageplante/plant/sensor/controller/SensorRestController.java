package com.polytech.arrosageplante.plant.sensor.controller;

import com.polytech.arrosageplante.plant.sensor.domain.Sensor;
import com.polytech.arrosageplante.plant.sensor.service.SensorCrudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SensorRestController {
    private final SensorCrudService sensorCrudService;

    public SensorRestController(SensorCrudService sensorCrudService) {
        this.sensorCrudService = sensorCrudService;
    }

    @GetMapping("sensor")
    public List<Sensor> getSensors() {
        return this.sensorCrudService.getSensors();
    }
}
