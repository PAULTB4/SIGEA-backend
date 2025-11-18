package com.zentry.sigea.module_actividad.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ActividadDomainEntity {
    private String actividadId;
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Aqui no seria necesario llamar a una entidad, solo necesitaria esta info para poder operar
    private EstadoActividadDomainEntity estadoActividadDomainEntity;
    
    // Uso una relacion completa para que se pueda obtener mas informacion
    private String organizadorId;

    // Aqui tampoco seria necesario llamar a una entidad
    private TipoActividadDomainEntity tipoActividadDomainEntity;
    
    private String lugar;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoActividadDomainEntity getEstadoActividadDomainEntity() {
        return estadoActividadDomainEntity;
    }
    public void setEstadoActividadDomainEntity(EstadoActividadDomainEntity estadoActividadDomainEntity) {
        this.estadoActividadDomainEntity = estadoActividadDomainEntity;
    }

    public String getOrganizadorId() {
        return organizadorId;
    }
    public void setOrganizadorId(String organizadorId) {
        this.organizadorId = organizadorId;
    }
    
    public TipoActividadDomainEntity getTipoActividadDomainEntity() {
        return tipoActividadDomainEntity;
    }
    public void setTipoActividadDomainEntity(TipoActividadDomainEntity tipoActividadDomainEntity) {
        this.tipoActividadDomainEntity = tipoActividadDomainEntity;
    }

    public String getLugar() {
        return lugar;
    }
    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    /* METODOS DEL DOMINIO */

    public String getActividadId() {
        return actividadId;
    }

    public void setActividadId(String actividadId) {
        this.actividadId = actividadId;
    }

    public static ActividadDomainEntity create(
        String titulo, 
        String descripcion,
        LocalDate fechaInicio, 
        LocalDate fechaFin,
        EstadoActividadDomainEntity estadoActividadDomainEntity, 
        String organizadorId,
        TipoActividadDomainEntity tipoActividadDomainEntity, 
        String lugar
    ) {
        
        LocalDateTime now = LocalDateTime.now();

        ActividadDomainEntity actividadDomainEntity = new ActividadDomainEntity();

        actividadDomainEntity.setTitulo(titulo);
        actividadDomainEntity.setDescripcion(descripcion);
        actividadDomainEntity.setFechaInicio(fechaInicio);
        actividadDomainEntity.setFechaFin(fechaFin);
        actividadDomainEntity.setEstadoActividadDomainEntity(estadoActividadDomainEntity);
        actividadDomainEntity.setOrganizadorId(organizadorId);
        actividadDomainEntity.setTipoActividadDomainEntity(tipoActividadDomainEntity);
        
        actividadDomainEntity.setCreatedAt(now);
        actividadDomainEntity.setUpdatedAt(now);

        return actividadDomainEntity;
    }

    // Métodos de negocio
    public void updateInfo(
        String titulo, 
        String descripcion, 
        LocalDate fechaInicio, 
        LocalDate fechaFin, 
        String lugar
    ) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(EstadoActividadDomainEntity estadoActividadDomainEntity) {
        if (estadoActividadDomainEntity == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estadoActividadDomainEntity = estadoActividadDomainEntity;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(fechaInicio) && !today.isAfter(fechaFin);
    }

    public boolean isFinished() {
        return LocalDate.now().isAfter(fechaFin);
    }

    public boolean isPending() {
        return LocalDate.now().isBefore(fechaInicio);
    }

    public long getDurationInDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

}
