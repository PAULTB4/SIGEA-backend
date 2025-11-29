package com.zentry.sigea.module_notificaciones.infrastructure.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuración para el procesamiento asíncrono de eventos de notificaciones
 * Permite que las notificaciones se procesen en segundo plano sin bloquear
 * las operaciones principales (pagos, inscripciones, etc.)
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo configura el procesamiento asíncrono
 * - Open/Closed: Extendible para agregar más configuraciones de async
 */
@Configuration
@EnableAsync
public class AsyncEventConfig {
    
    /**
     * Configura el executor para procesar eventos de notificaciones de forma asíncrona
     * Thread pool optimizado para IO (envío de emails, WhatsApp, etc.)
     */
    @Bean(name = "notificationEventExecutor")
    public Executor notificationEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Core threads: Siempre activos
        executor.setCorePoolSize(2);
        
        // Max threads: Máximo de threads que pueden crearse
        executor.setMaxPoolSize(5);
        
        // Queue capacity: Tamaño de cola de espera
        executor.setQueueCapacity(100);
        
        // Thread name prefix para facilitar debugging en logs
        executor.setThreadNamePrefix("NotificationEvent-");
        
        // Política cuando se alcanza el límite: El caller ejecuta la tarea
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        
        // Esperar a que terminen todas las tareas al hacer shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
}
