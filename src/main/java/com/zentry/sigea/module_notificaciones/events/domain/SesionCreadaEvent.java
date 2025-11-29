package com.zentry.sigea.module_notificaciones.events.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Evento de dominio que representa la creación de una nueva sesión
 * Se publica cuando se programa una sesión para una actividad
 */
public class SesionCreadaEvent {
    
    private final String sesionId;
    private final String actividadId;
    private final String titulo;
    private final LocalDateTime fechaSesion;
    private final List<String> usuariosIds; // Usuarios inscritos en la actividad que deben ser notificados
    private final LocalDateTime fechaEvento;
    
    public SesionCreadaEvent(
        String sesionId,
        String actividadId,
        String titulo,
        LocalDateTime fechaSesion,
        List<String> usuariosIds,
        LocalDateTime fechaEvento
    ) {
        this.sesionId = sesionId;
        this.actividadId = actividadId;
        this.titulo = titulo;
        this.fechaSesion = fechaSesion;
        this.usuariosIds = usuariosIds;
        this.fechaEvento = fechaEvento;
    }
    
    // Getters
    public String getSesionId() {
        return sesionId;
    }
    
    public String getActividadId() {
        return actividadId;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public LocalDateTime getFechaSesion() {
        return fechaSesion;
    }
    
    public List<String> getUsuariosIds() {
        return usuariosIds;
    }
    
    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }
}
