package com.zentry.sigea.module_notificaciones.events.domain;

import java.time.LocalDateTime;

/**
 * Evento de dominio que se publica cuando se crea una nueva inscripción
 * Este evento dispara automáticamente una notificación al usuario
 */
public class InscripcionCreadaEvent {
    
    private final String usuarioId;
    private final String actividadId;
    private final String inscripcionId;
    private final LocalDateTime fechaInscripcion;
    
    public InscripcionCreadaEvent(
        String usuarioId, 
        String actividadId, 
        String inscripcionId,
        LocalDateTime fechaInscripcion
    ) {
        this.usuarioId = usuarioId;
        this.actividadId = actividadId;
        this.inscripcionId = inscripcionId;
        this.fechaInscripcion = fechaInscripcion;
    }
    
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public String getActividadId() {
        return actividadId;
    }
    
    public String getInscripcionId() {
        return inscripcionId;
    }
    
    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }
}
