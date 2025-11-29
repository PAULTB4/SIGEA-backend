package com.zentry.sigea.module_notificaciones.core.entities;

/**
 * Entidad de dominio para estados de notificación
 * Representa los diferentes estados de una notificación (Enviada, Leída, Pendiente, etc.)
 */
public class EstadoNotificacionDomainEntity {
    private String id;
    private String codigo;
    private String etiqueta;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // Métodos de dominio
    /**
     * Factory method para crear un nuevo estado de notificación
     */
    public static EstadoNotificacionDomainEntity create(String codigo, String etiqueta) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        if (codigo.length() > 30) {
            throw new IllegalArgumentException("El código no debe tener más de 30 caracteres");
        }
        if (etiqueta != null && etiqueta.length() > 60) {
            throw new IllegalArgumentException("La etiqueta no debe tener más de 60 caracteres");
        }

        EstadoNotificacionDomainEntity estado = new EstadoNotificacionDomainEntity();
        estado.setCodigo(codigo.trim().toUpperCase());
        estado.setEtiqueta(etiqueta != null ? etiqueta.trim() : null);
        return estado;
    }
}
