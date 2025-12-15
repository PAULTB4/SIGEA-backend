package com.zentry.sigea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling // Habilita las tareas cada cierto tiempo

public class SigeaApplication {
public static void main(String[] args) {
        // Cargar .env solo si existe (Local), si no, ignorar (Nube)
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            System.out.println("Archivo .env no encontrado. Usando variables de entorno del sistema.");
        }

        SpringApplication.run(SigeaApplication.class, args);
    }
}
