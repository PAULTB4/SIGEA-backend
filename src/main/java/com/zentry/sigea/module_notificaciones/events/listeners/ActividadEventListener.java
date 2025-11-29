package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.ActividadCreadaEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

/**
 * Listener que escucha eventos de actividades creadas y genera notificaciones automáticas
 * Principio: Single Responsibility - Solo maneja notificaciones de nuevas actividades
 */
@Component
public class ActividadEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(ActividadEventListener.class);
    private static final String TIPO_NOTIFICACION_ACTIVIDAD = "COMUNICACION";
    
    private final NotificacionService notificacionService;
    
    public ActividadEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Escucha el evento de actividad creada y genera notificaciones automáticas
     * Envía notificación a todos los usuarios especificados en el evento
     * @Async permite que las notificaciones se procesen en segundo plano
     */
    @EventListener
    @Async
    public void onActividadCreada(ActividadCreadaEvent event) {
        try {
            logger.info("Evento recibido: Nueva actividad creada '{}' con {} destinatarios", 
                event.getTitulo(), event.getUsuariosIds().size());
            
            // Enviar notificación a cada usuario
            for (String usuarioId : event.getUsuariosIds()) {
                try {
                    // Constructor: usuarioId, actividadId, tipoNotificacionId, mensaje, estadoNotificacionId, canal
                    CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                        usuarioId,
                        event.getActividadId(),
                        TIPO_NOTIFICACION_ACTIVIDAD,
                        String.format("🎯 Se ha publicado una nueva actividad: \"%s\"\n\n" +
                                     "📝 %s\n\n" +
                                     "¡No te la pierdas! Inscribete ahora.", 
                            event.getTitulo(), event.getDescripcion()),
                        null, // estadoNotificacionId (el service lo asigna)
                        "SISTEMA"
                    );
                    
                    String resultado = notificacionService.crearNotificacion(notificacionRequest);
                    logger.info("Notificación de nueva actividad enviada a usuario {}: {}", usuarioId, resultado);
                    
                } catch (Exception e) {
                    logger.error("Error al enviar notificación a usuario {}: {}", usuarioId, e.getMessage());
                    // Continuar con los demás usuarios aunque falle uno
                }
            }
            
            logger.info("Proceso de notificaciones de actividad completado");
            
        } catch (Exception e) {
            logger.error("Error al procesar notificaciones de actividad creada: {}", e.getMessage(), e);
            // No lanzamos la excepción para no afectar el flujo principal
        }
    }
}
