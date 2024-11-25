package com.polytech.arrosageplante.plant.sensor.service;

import com.polytech.arrosageplante.exception.EntityNotFound;
import com.polytech.arrosageplante.plant.sensor.domain.Sensor;
import com.polytech.arrosageplante.plant.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorCrudService {
    private final SensorRepository sensorRepository;

    public SensorCrudService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getSensors() {
        return this.sensorRepository.findAll();
    }

    public Sensor getSensor(long id) {
        return this.sensorRepository.findById(id).orElseThrow(EntityNotFound::new);
    }
}
