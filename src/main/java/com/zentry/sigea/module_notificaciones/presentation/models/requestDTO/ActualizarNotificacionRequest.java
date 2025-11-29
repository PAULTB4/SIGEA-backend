package com.zentry.sigea.module_notificaciones.presentation.models.requestDTO;

/**
 * DTO para actualizar una notificación existente
 */
public class ActualizarNotificacionRequest {
    private String mensaje;
    private String estadoNotificacionId;

    // Constructor vacío para Jackson
    public ActualizarNotificacionRequest() {}

    public ActualizarNotificacionRequest(String mensaje, String estadoNotificacionId) {
        this.mensaje = mensaje;
        this.estadoNotificacionId = estadoNotificacionId;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstadoNotificacionId() {
        return estadoNotificacionId;
    }

    public void setEstadoNotificacionId(String estadoNotificacionId) {
        this.estadoNotificacionId = estadoNotificacionId;
    }
}
