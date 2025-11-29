package com.zentry.sigea.module_notificaciones.presentation.models.responseDTO;

import java.time.LocalDateTime;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;

/**
 * DTO para enviar datos de notificación al frontend
 * Simplificado - el estado se obtiene desde estadoNotificacion.codigo
 */
public class NotificacionResponse {
    private String id;
    private String usuarioId;
    private String actividadId;
    private TipoNotificacionDomainEntity tipoNotificacion;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private EstadoNotificacionDomainEntity estadoNotificacion;
    private String canal; // SISTEMA, CORREO, WHATSAPP

    // Constructor vacío para Jackson
    public NotificacionResponse() {}

    public NotificacionResponse(
        String id,
        String usuarioId,
        String actividadId,
        TipoNotificacionDomainEntity tipoNotificacion,
        String mensaje,
        LocalDateTime fechaEnvio,
        EstadoNotificacionDomainEntity estadoNotificacion,
        String canal
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.actividadId = actividadId;
        this.tipoNotificacion = tipoNotificacion;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.estadoNotificacion = estadoNotificacion;
        this.canal = canal;
    }

    /**
     * Factory method para crear un NotificacionResponse desde una entidad de dominio
     */
    public static NotificacionResponse fromEntity(NotificacionDomainEntity notificacion) {
        return new NotificacionResponse(
            notificacion.getId(),
            notificacion.getUsuarioId(),
            notificacion.getActividadId(),
            notificacion.getTipoNotificacion(),
            notificacion.getMensaje(),
            notificacion.getFechaEnvio(),
            notificacion.getEstadoNotificacion(),
            notificacion.getCanal() != null ? notificacion.getCanal().name() : null
        );
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getActividadId() {
        return actividadId;
    }

    public void setActividadId(String actividadId) {
        this.actividadId = actividadId;
    }

    public TipoNotificacionDomainEntity getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacionDomainEntity tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public EstadoNotificacionDomainEntity getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(EstadoNotificacionDomainEntity estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }
}
