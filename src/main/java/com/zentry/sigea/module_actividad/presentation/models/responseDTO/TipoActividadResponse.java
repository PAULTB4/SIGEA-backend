package com.zentry.sigea.module_actividad.presentation.models.responseDTO;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;

public class TipoActividadResponse {
    private String id;
    private String nombreActividad;
    private String descripcion;

    public TipoActividadResponse() {
    }

    public TipoActividadResponse(String id, String nombreActividad, String descripcion) {
        this.id = id;
        this.nombreActividad = nombreActividad;
        this.descripcion = descripcion;
    }


    /**
     * Factory method para crear un TipoActividadResponse desde una entidad TipoActividad
     */

    public static TipoActividadResponse fromEntity(TipoActividadDomainEntity tipoActividad) {
        
        return new TipoActividadResponse(
            tipoActividad.getTipoActividadId(),
            tipoActividad.getNombreActividad(),
            tipoActividad.getDescripcion()
        );
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombreActividad() {
        return nombreActividad;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
