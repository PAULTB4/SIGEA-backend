package com.zentry.sigea.module_notificaciones.core.entities;

/**
 * Entidad de dominio para tipos de notificación
 * Representa los diferentes tipos de notificaciones del sistema
 */
public class TipoNotificacionDomainEntity {
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
     * Factory method para crear un nuevo tipo de notificación
     */
    public static TipoNotificacionDomainEntity create(String codigo, String etiqueta) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        if (codigo.length() > 30) {
            throw new IllegalArgumentException("El código no debe tener más de 30 caracteres");
        }
        if (etiqueta != null && etiqueta.length() > 60) {
            throw new IllegalArgumentException("La etiqueta no debe tener más de 60 caracteres");
        }

        TipoNotificacionDomainEntity tipo = new TipoNotificacionDomainEntity();
        tipo.setCodigo(codigo.trim().toUpperCase());
        tipo.setEtiqueta(etiqueta != null ? etiqueta.trim() : null);
        return tipo;
    }
}
