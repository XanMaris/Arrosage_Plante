package com.polytech.arrosageplante.plant.repository;

import com.polytech.arrosageplante.plant.domain.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, String> {
    Optional<Plant> findPlantByEsp32PrivateCode(String privateCode);

    List<Plant> findAllByAutomaticWatering(boolean automaticWatering);
}
