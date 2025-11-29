package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

/**
 * Listener que escucha eventos de inscripciones y genera notificaciones automáticas
 * Principio: Single Responsibility - Solo maneja notificaciones de inscripciones
 */
@Component
public class InscripcionEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(InscripcionEventListener.class);
    private static final String TIPO_NOTIFICACION_INSCRIPCION = "INSCRIPCION";
    
    private final NotificacionService notificacionService;
    
    public InscripcionEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Escucha el evento de inscripción creada y genera notificación automática
     * @Async permite que la notificación se procese en segundo plano sin bloquear la inscripción
     */
    @EventListener
    @Async
    public void onInscripcionCreada(InscripcionCreadaEvent event) {
        try {
            logger.info("Evento recibido: Nueva inscripción para usuario {} en actividad {}", 
                event.getUsuarioId(), event.getActividadId());
            
            // Constructor: usuarioId, actividadId, tipoNotificacionId, mensaje, estadoNotificacionId, canal
                CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                    event.getUsuarioId(),
                    event.getActividadId(),
                    TIPO_NOTIFICACION_INSCRIPCION,
                    "✅ ¡Inscripcion confirmada exitosamente!\n\n" +
                    "Tu inscripcion ha sido registrada correctamente. " +
                    "Recibiras notificaciones sobre las sesiones y actualizaciones de esta actividad.\n\n" +
                    "¡Te esperamos!",
                    null, // estadoNotificacionId (el service lo asigna)
                    "SISTEMA" // Canal sin .name() ya que es String
                );            String resultado = notificacionService.crearNotificacion(notificacionRequest);
            
            logger.info("Notificación automática de inscripción enviada: {}", resultado);
            
        } catch (Exception e) {
            logger.error("Error al procesar notificación de inscripción para usuario {}: {}", 
                event.getUsuarioId(), e.getMessage(), e);
            // No lanzamos la excepción para no afectar el flujo principal de inscripción
        }
    }
}
