package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.ActividadCreadaEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;
import com.zentry.sigea.module_usuarios.services.UsuarioService;

@Component
public class ActividadEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ActividadEventListener.class);
    private static final String TIPO_NOTIFICACION_ACTIVIDAD = "COMUNICACION";

    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;

    public ActividadEventListener(
            NotificacionService notificacionService,
            UsuarioService usuarioService
    ) {
        this.notificacionService = notificacionService;
        this.usuarioService = usuarioService;
    }

    @EventListener
    @Async
    public void onActividadCreada(ActividadCreadaEvent event) {
        try {
            logger.info("Evento recibido: Nueva actividad '{}' enviada a {} destinatarios",
                    event.getTitulo(), event.getUsuariosIds().size());

            for (String usuarioId : event.getUsuariosIds()) {
                try {
                    // Mensaje completo para BD y email
                    String mensaje = String.format(
                        "Actividad: %s, Descripción: %s",
                        event.getTitulo(),
                        event.getDescripcion() != null ? event.getDescripcion() : ""
                    );

                    CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                        usuarioId,
                        event.getActividadId(),
                        TIPO_NOTIFICACION_ACTIVIDAD,
                        mensaje,
                        null,
                        "SISTEMA"
                    );

                    String resultado = notificacionService.crearNotificacion(notificacionRequest);
                    logger.info("Notificación enviada a usuario {}: {}", usuarioId, resultado);

                } catch (Exception e) {
                    logger.error("Error notificando al usuario {}: {}", usuarioId, e.getMessage());
                }
            }

            logger.info("Proceso de notificaciones completado.");

        } catch (Exception e) {
            logger.error("Error general en listener de actividad creada: {}", e.getMessage(), e);
        }
    }
}
