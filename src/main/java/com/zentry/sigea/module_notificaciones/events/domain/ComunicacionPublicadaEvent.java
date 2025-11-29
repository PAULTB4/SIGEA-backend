package com.zentry.sigea.module_notificaciones.events.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Evento de dominio que se publica cuando hay una comunicación importante
 * (nueva sesión, anuncio, cambio en actividad, etc.)
 * Este evento dispara automáticamente notificaciones a múltiples usuarios
 */
public class ComunicacionPublicadaEvent {
    
    private final List<String> usuariosIds;
    private final String actividadId;
    private final String titulo;
    private final String mensaje;
    private final TipoComunicacion tipo;
    private final LocalDateTime fechaPublicacion;
    
    public enum TipoComunicacion {
        NUEVA_SESION,
        CAMBIO_SESION,
        CANCELACION_SESION,
        ANUNCIO_ACTIVIDAD,
        RECORDATORIO,
        COMUNICADO_GENERAL
    }
    
    public ComunicacionPublicadaEvent(
        List<String> usuariosIds,
        String actividadId,
        String titulo,
        String mensaje,
        TipoComunicacion tipo,
        LocalDateTime fechaPublicacion
    ) {
        this.usuariosIds = usuariosIds;
        this.actividadId = actividadId;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaPublicacion = fechaPublicacion;
    }
    
    public List<String> getUsuariosIds() {
        return usuariosIds;
    }
    
    public String getActividadId() {
        return actividadId;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public TipoComunicacion getTipo() {
        return tipo;
    }
    
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }
}
