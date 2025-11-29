package com.zentry.sigea.module_inscripciones.core.entities;

/**
 * Entidad de dominio para el estado de inscripción
 * Representa los estados posibles: PENDIENTE | CONFIRMADA | CANCELADA
 */
public class EstadoInscripcionDomainEntity {
    private String id;
    private String codigo;
    private String etiqueta;

    public static EstadoInscripcionDomainEntity create(
        String codigo, 
        String etiqueta
    ) {
        EstadoInscripcionDomainEntity estadoInscripcionDomainEntity = new EstadoInscripcionDomainEntity();

        estadoInscripcionDomainEntity.setCodigo(codigo);
        estadoInscripcionDomainEntity.setEtiqueta(etiqueta);

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser nulo o vacío");
        }

        return estadoInscripcionDomainEntity;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
