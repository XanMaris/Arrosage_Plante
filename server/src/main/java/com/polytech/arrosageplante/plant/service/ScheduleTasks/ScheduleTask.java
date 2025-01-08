package com.polytech.arrosageplante.plant.service.ScheduleTasks;

import com.polytech.arrosageplante.esp32.communication.websocket.output.WebSocketPublisherService;
import com.polytech.arrosageplante.plant.domain.Plant;
import com.polytech.arrosageplante.plant.repository.PlantRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    private WebSocketPublisherService webSockerPlantService;

    private PlantRepository plantRepository;;

    public ScheduleTask(WebSocketPublisherService webSockerPlantService, PlantRepository plantRepository) {
        this.webSockerPlantService = webSockerPlantService;
        this.plantRepository = plantRepository;
    }
    // La méthode s'exécutera tous les jours à 12h00
    @Scheduled(cron = "0 00 12 * * ?")
    public void executeTaskAtNoon() {
        for (Plant plant : this.plantRepository.findAllByAutomaticWatering(true)) {
            try {
                    this.webSockerPlantService.arroser(plant);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'arrosage de la plante " + plant.getId() + " : " + e.getMessage());
            }
        }
    }
}
