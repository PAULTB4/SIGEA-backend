package com.zentry.sigea.module_notificaciones.core.entities;

import java.time.LocalDateTime;

/**
 * Entidad de dominio para notificaciones
 * Representa una notificación enviada a un usuario del sistema
 */
public class NotificacionDomainEntity {
    private String id;
    private String usuarioId;
    private String actividadId; // Puede ser null si no está relacionada a una actividad
    private TipoNotificacionDomainEntity tipoNotificacion;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private EstadoNotificacionDomainEntity estadoNotificacion;
    private CanalNotificacion canal; // Canal por el que se envía la notificación
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public CanalNotificacion getCanal() {
        return canal;
    }

    public void setCanal(CanalNotificacion canal) {
        this.canal = canal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /* MÉTODOS DEL DOMINIO */

    /**
     * Factory method para crear una nueva notificación
     */
    public static NotificacionDomainEntity create(
        String usuarioId,
        String actividadId,
        TipoNotificacionDomainEntity tipoNotificacion,
        String mensaje,
        EstadoNotificacionDomainEntity estadoNotificacion,
        CanalNotificacion canal
    ) {
        LocalDateTime now = LocalDateTime.now();

        // Validaciones
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        if (tipoNotificacion == null) {
            throw new IllegalArgumentException("El tipo de notificación es obligatorio");
        }
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje es obligatorio");
        }
        if (estadoNotificacion == null) {
            throw new IllegalArgumentException("El estado de notificación es obligatorio");
        }
        if (canal == null) {
            throw new IllegalArgumentException("El canal es obligatorio");
        }

        NotificacionDomainEntity notificacion = new NotificacionDomainEntity();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setActividadId(actividadId);
        notificacion.setTipoNotificacion(tipoNotificacion);
        notificacion.setMensaje(mensaje.trim());
        notificacion.setFechaEnvio(now);
        notificacion.setEstadoNotificacion(estadoNotificacion);
        notificacion.setCanal(canal);
        notificacion.setCreatedAt(now);
        notificacion.setUpdatedAt(now);

        return notificacion;
    }

    /**
     * Cambia el estado de la notificación
     */
    public void cambiarEstado(EstadoNotificacionDomainEntity nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estadoNotificacion = nuevoEstado;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca la notificación como leída
     */
    public void marcarComoLeida(EstadoNotificacionDomainEntity estadoLeida) {
        if (estadoLeida == null || !"LEIDA".equals(estadoLeida.getCodigo())) {
            throw new IllegalArgumentException("El estado debe ser 'LEIDA'");
        }
        this.cambiarEstado(estadoLeida);
    }

    /**
     * Verifica si la notificación está pendiente de ser leída
     */
    public boolean estaPendiente() {
        return estadoNotificacion != null && 
               "PENDIENTE".equals(estadoNotificacion.getCodigo());
    }

    /**
     * Verifica si la notificación ya fue leída
     */
    public boolean estaLeida() {
        return estadoNotificacion != null && 
               "LEIDA".equals(estadoNotificacion.getCodigo());
    }

    /**
     * Verifica si la notificación fue enviada exitosamente
     */
    public boolean estaEnviada() {
        return estadoNotificacion != null && 
               "ENVIADA".equals(estadoNotificacion.getCodigo());
    }

    /**
     * Verifica si la notificación es importante según su tipo
     * Las notificaciones de certificados e inscripciones se consideran importantes
     */
    public boolean esImportante() {
        if (tipoNotificacion == null || tipoNotificacion.getCodigo() == null) {
            return false;
        }
        String codigo = tipoNotificacion.getCodigo().toUpperCase();
        return codigo.contains("CERTIFICADO") || 
               codigo.contains("INSCRIPCION") || 
               codigo.equals("IMPORTANTE");
    }
}
