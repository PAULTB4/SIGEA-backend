package com.zentry.sigea.module_notificaciones.core.entities;

/**
 * Enum para los canales de notificación disponibles en el sistema
 */
public enum CanalNotificacion {
    /**
     * Notificación dentro del sistema (notificaciones push en la aplicación)
     */
    SISTEMA("Sistema", "Notificación mostrada dentro de la aplicación"),
    
    /**
     * Notificación por correo electrónico
     */
    CORREO("Correo Electrónico", "Notificación enviada por email"),
    
    /**
     * Notificación por WhatsApp
     */
    WHATSAPP("WhatsApp", "Notificación enviada por WhatsApp");

    private final String nombre;
    private final String descripcion;

    CanalNotificacion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el canal desde un string, lanzando excepción si no es válido
     */
    public static CanalNotificacion fromString(String canal) {
        if (canal == null) {
            throw new IllegalArgumentException("El canal no puede ser nulo");
        }
        
        try {
            return CanalNotificacion.valueOf(canal.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Canal inválido: " + canal + ". Los canales válidos son: SISTEMA, CORREO, WHATSAPP"
            );
        }
    }

    /**
     * Verifica si el canal requiere información adicional (email, teléfono)
     */
    public boolean requiereContacto() {
        return this == CORREO || this == WHATSAPP;
    }
}
