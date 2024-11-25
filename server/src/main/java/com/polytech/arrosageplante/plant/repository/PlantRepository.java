package com.polytech.arrosageplante.plant.repository;

import com.polytech.arrosageplante.plant.domain.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
}
