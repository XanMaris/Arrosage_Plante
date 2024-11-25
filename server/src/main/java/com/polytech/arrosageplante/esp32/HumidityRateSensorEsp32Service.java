package com.polytech.arrosageplante.esp32;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumidityRateSensorEsp32Service {

    // TODO: requests ESP32
    public double getCurrentHumidityRate(long id) {
        return 0.5;
    }

    public List<Long> getAll() {
        return List.of(1l); // TODO: replace it by requests ESP-32
    }
}
