package com.zentry.sigea.module_actividad.core.entities;

public class EstadoActividadDomainEntity {
    private String estadoActividadId;
    private String codigo;
    private String etiqueta;

    public static EstadoActividadDomainEntity create(String codigo, String etiqueta) {
        EstadoActividadDomainEntity estadoActividadDomainEntity = new EstadoActividadDomainEntity();

        estadoActividadDomainEntity.setCodigo(codigo);
        estadoActividadDomainEntity.setEtiqueta(etiqueta);

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser nulo o vacío");
        }

        return estadoActividadDomainEntity;
    }
    public String getEstadoActividadId() {
        return estadoActividadId;
    }
    public void setEstadoActividadId(String estadoActividadId) {
        this.estadoActividadId = estadoActividadId;
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

}
