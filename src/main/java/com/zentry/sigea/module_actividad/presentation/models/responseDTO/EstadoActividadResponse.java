package com.zentry.sigea.module_actividad.presentation.models.responseDTO;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;

public class EstadoActividadResponse {
    private String id;
    private String codigo;
    private String etiqueta;

    public EstadoActividadResponse() {
    }

    public EstadoActividadResponse(String id, String codigo, String etiqueta) {
        this.id = id;
        this.codigo = codigo;
        this.etiqueta = etiqueta;
    }


    public static EstadoActividadResponse fromEntity(EstadoActividadDomainEntity estadoActividad) {
        return new EstadoActividadResponse(
            estadoActividad.getEstadoActividadId(),
            estadoActividad.getCodigo(),
            estadoActividad.getEtiqueta()
        );
    }

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
    
}
