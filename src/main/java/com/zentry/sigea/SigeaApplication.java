package com.zentry.sigea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling // Habilita las tareas cada cierto tiempo
public class SigeaApplication {

	public static void main(String[] args) {
		// Cargar variables en .env antes de iniciar la app
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

		SpringApplication.run(SigeaApplication.class, args);
	}

}
