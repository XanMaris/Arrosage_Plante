package com.polytech.arrosageplante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArrosageplanteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArrosageplanteApplication.class, args);
	}

}
