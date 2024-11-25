package com.polytech.arrosageplante.plant.sensor.repository;

import com.polytech.arrosageplante.plant.sensor.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
