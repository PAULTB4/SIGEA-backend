package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

/**
 * Listener que escucha eventos de comunicaciones y genera notificaciones automáticas
 * Maneja anuncios, nuevas sesiones, cambios, etc.
 * Principio: Single Responsibility - Solo maneja notificaciones de comunicaciones
 */
@Component
public class ComunicacionEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(ComunicacionEventListener.class);
    private static final String TIPO_NOTIFICACION_COMUNICACION = "COMUNICACION";
    
    private final NotificacionService notificacionService;
    
    public ComunicacionEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Escucha el evento de comunicación publicada y genera notificaciones automáticas
     * Puede enviar a múltiples usuarios (broadcast)
     * @Async permite que las notificaciones se procesen en segundo plano
     */
    @EventListener
    @Async
    public void onComunicacionPublicada(ComunicacionPublicadaEvent event) {
        try {
            logger.info("Evento recibido: Comunicación tipo {} para {} usuarios", 
                event.getTipo(), event.getUsuariosIds().size());
            
            // Enviar notificación a cada usuario
            for (String usuarioId : event.getUsuariosIds()) {
                try {
                    // Constructor correcto: usuarioId, actividadId, tipoNotificacionId, mensaje, estadoNotificacionId, canal
                    CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                        usuarioId,
                        event.getActividadId(),
                        TIPO_NOTIFICACION_COMUNICACION,
                        String.format("%s %s\n\n%s", 
                            obtenerIconoPorTipo(event.getTipo()), 
                            event.getTitulo(), 
                            event.getMensaje()),
                        null, // estadoNotificacionId (el service lo asigna)
                        "SISTEMA"
                    );
                    
                    String resultado = notificacionService.crearNotificacion(notificacionRequest);
                    logger.info("Comunicacion enviada a usuario {}: {}", usuarioId, resultado);
                    
                } catch (Exception e) {
                    logger.error("Error al enviar notificación a usuario {}: {}", 
                        usuarioId, e.getMessage());
                    // Continuar con los demás usuarios aunque falle uno
                }
            }
            
            logger.info("Notificaciones automáticas de comunicación enviadas a {} usuarios", 
                event.getUsuariosIds().size());
            
        } catch (Exception e) {
            logger.error("Error al procesar comunicación: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene un emoji apropiado según el tipo de comunicación
     */
    private String obtenerIconoPorTipo(ComunicacionPublicadaEvent.TipoComunicacion tipo) {
        return switch (tipo) {
            case NUEVA_SESION -> "📅";
            case CAMBIO_SESION -> "🔄";
            case CANCELACION_SESION -> "❌";
            case ANUNCIO_ACTIVIDAD -> "📢";
            case RECORDATORIO -> "⏰";
            case COMUNICADO_GENERAL -> "📣";
        };
    }
}
