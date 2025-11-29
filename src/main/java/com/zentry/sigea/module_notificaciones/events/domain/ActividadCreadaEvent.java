package com.zentry.sigea.module_notificaciones.events.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Evento de dominio que se publica cuando se crea una nueva actividad
 * Este evento dispara automáticamente notificaciones a los usuarios interesados
 */
public class ActividadCreadaEvent {
    
    private final String actividadId;
    private final String titulo;
    private final String descripcion;
    private final List<String> usuariosIds; // Usuarios a notificar (ej: organizadores, admins, suscriptores)
    private final LocalDateTime fechaCreacion;
    
    public ActividadCreadaEvent(
        String actividadId,
        String titulo,
        String descripcion,
        List<String> usuariosIds,
        LocalDateTime fechaCreacion
    ) {
        this.actividadId = actividadId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuariosIds = usuariosIds;
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getActividadId() {
        return actividadId;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public List<String> getUsuariosIds() {
        return usuariosIds;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
