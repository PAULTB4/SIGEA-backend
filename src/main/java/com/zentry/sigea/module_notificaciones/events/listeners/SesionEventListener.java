package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.SesionCreadaEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import java.time.format.DateTimeFormatter;

/**
 * Listener que escucha eventos de sesiones creadas y genera notificaciones automáticas
 * Principio: Single Responsibility - Solo maneja notificaciones de nuevas sesiones
 */
@Component
public class SesionEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(SesionEventListener.class);
    private static final String TIPO_NOTIFICACION_SESION = "COMUNICACION";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private final NotificacionService notificacionService;
    
    public SesionEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Escucha el evento de sesión creada y genera notificaciones automáticas
     * Envía notificación a todos los usuarios inscritos en la actividad
     * @Async permite que las notificaciones se procesen en segundo plano
     */
    @EventListener
    @Async
    public void onSesionCreada(SesionCreadaEvent event) {
        try {
            logger.info("Evento recibido: Nueva sesion creada '{}' para actividad {} con {} destinatarios", 
                event.getTitulo(), event.getActividadId(), event.getUsuariosIds().size());
            
            String fechaFormateada = event.getFechaSesion().format(FORMATTER);
            
            // Enviar notificación a cada usuario inscrito
            for (String usuarioId : event.getUsuariosIds()) {
                try {
                    CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                        usuarioId,
                        event.getActividadId(),
                        TIPO_NOTIFICACION_SESION,
                        String.format("📅 Se ha programado una nueva sesion: \"%s\"\n\n" +
                                     "📍 Fecha y hora: %s\n\n" +
                                     "Esta sesion forma parte de la actividad en la que estas inscrito. " +
                                     "Te esperamos!", 
                            event.getTitulo(), fechaFormateada),
                        null, // estadoNotificacionId (el service lo asigna)
                        "SISTEMA"
                    );
                    
                    String resultado = notificacionService.crearNotificacion(notificacionRequest);
                    logger.info("Notificacion de nueva sesion enviada a usuario {}: {}", usuarioId, resultado);
                    
                } catch (Exception e) {
                    logger.error("Error al enviar notificacion a usuario {}: {}", usuarioId, e.getMessage());
                    // Continuar con los demás usuarios aunque falle uno
                }
            }
            
            logger.info("Notificaciones de sesion procesadas exitosamente para {} usuarios", 
                event.getUsuariosIds().size());
            
        } catch (Exception e) {
            logger.error("Error al procesar evento de sesion creada: {}", e.getMessage(), e);
        }
    }
}
