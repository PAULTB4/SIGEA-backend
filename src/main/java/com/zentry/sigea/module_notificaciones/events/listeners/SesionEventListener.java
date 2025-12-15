package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.SesionCreadaEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

@Component
public class SesionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SesionEventListener.class);
    private static final String TIPO_NOTIFICACION_SESION = "COMUNICACION";

    private final NotificacionService notificacionService;
    // private final ActividadService actividadService;

    public SesionEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @EventListener
    @Async
    public void onSesionCreada(SesionCreadaEvent event) {

        try {
            logger.info(
                "Evento recibido: Nueva sesión '{}' en actividad {} para {} usuarios",
                event.getTitulo(),
                event.getActividadId(),
                event.getUsuariosIds().size()
            );

            for (String usuarioId : event.getUsuariosIds()) {
                try {

                    // Mensaje en texto plano, formato profesional y legible (guardar todo en la BD)
                    String mensaje = String.format(
                        "Nueva publicación: %s\n\n" +
                        ", Descripción:\n%s\n\n" +
                        ", Ponente: %s\n" +
                        ", Modalidad: %s\n" +
                        ", Lugar: %s\n" +
                        ", Fecha: %s\n" +
                        ", Hora inicio: %s\n" +
                        ", Hora fin: %s\n\n" +
                        "--IMPORTANTE:\n(Esta es una sesión de una actividad en la que te inscribiste), ¡No te la pierdas!",
                        event.getTitulo(),
                        event.getDescripcion() != null ? event.getDescripcion() : "",
                        event.getPonente(),
                        event.getModalidad(),
                        event.getLugar(),
                        event.getFechaSesion() != null ? event.getFechaSesion().toLocalDate().toString() : "",
                        event.getHoraInicio() != null ? event.getHoraInicio().toString() : "",
                        event.getHoraFin() != null ? event.getHoraFin().toString() : ""
                    );

                    CrearNotificacionRequest request = new CrearNotificacionRequest(
                        usuarioId,
                        event.getActividadId(),
                        TIPO_NOTIFICACION_SESION,
                        mensaje,
                        null,
                        "SISTEMA"
                    );
                    String result = notificacionService.crearNotificacion(request);
                    logger.info("Notificación enviada → Usuario {}: {}", usuarioId, result);

                    // Si tienes un método para enviar email, pásale mensaje (ya tiene todo)
                    // notificacionService.enviarEmail(usuarioId, mensaje);

                } catch (Exception e) {
                    logger.error("Error enviando notificación al usuario {}: {}", usuarioId, e.getMessage());
                }
            }

            logger.info("Notificaciones de sesión enviadas correctamente.");

        } catch (Exception e) {
            logger.error("Error procesando evento SesionCreada: {}", e.getMessage(), e);
        }
    }
}
