package com.zentry.sigea.module_actividad.core.entities;

import java.time.LocalDateTime;

public class TipoActividadDomainEntity {
    private String tipoActividadId;
    private String nombreActividad;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Factory method mejorado - maneja timestamps automáticamente
    public static TipoActividadDomainEntity create(String nombreActividad, String descripcion) {
        LocalDateTime now = LocalDateTime.now();

        TipoActividadDomainEntity tipoActividadDomainEntity = new TipoActividadDomainEntity();

        tipoActividadDomainEntity.setNombreActividad(nombreActividad);
        tipoActividadDomainEntity.setDescripcion(descripcion);
        tipoActividadDomainEntity.setCreatedAt(now);
        tipoActividadDomainEntity.setUpdatedAt(now);

        return tipoActividadDomainEntity;
    }

    public String getTipoActividadId() {
        return tipoActividadId;
    }

    public void setTipoActividadId(String tipoActividadId) {
        this.tipoActividadId = tipoActividadId;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }
    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
