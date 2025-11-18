package com.zentry.sigea.module_actividad.presentation.models.responseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;

/**
 * DTO para enviar datos de actividad al frontend
 */
public class ActividadResponse {
    private String id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoActividadResponse estado;
    private String organizadorId;
    private TipoActividadResponse tipoActividad;
    private String ubicacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para la vista
    private boolean activa;
    private boolean finalizada;
    private boolean pendiente;
    private Long duracionEnDias;

    // Constructor vacío para Jackson
    public ActividadResponse() {}

    public ActividadResponse(
        String id,
        String titulo, 
        String descripcion, 
        LocalDate fechaInicio, 
        LocalDate fechaFin, 
        EstadoActividadResponse estado, 
        String organizadorId, 
        TipoActividadResponse tipoActividad, 
        String ubicacion, 
        LocalDateTime fechaCreacion, 
        LocalDateTime fechaActualizacion, 
        boolean activa, 
        boolean finalizada, 
        boolean pendiente, 
        Long duracionEnDias
    ) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.organizadorId = organizadorId;
        this.tipoActividad = tipoActividad;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.activa = activa;
        this.finalizada = finalizada;
        this.pendiente = pendiente;
        this.duracionEnDias = duracionEnDias;
    }

    /**
     * Factory method para crear un ActividadResponse desde una entidad Actividad
     */
    public static ActividadResponse fromEntity(ActividadDomainEntity actividadDomainEntity) {
        return new ActividadResponse(
            actividadDomainEntity.getActividadId(),
            actividadDomainEntity.getTitulo(),
            actividadDomainEntity.getDescripcion(),
            actividadDomainEntity.getFechaInicio(),
            actividadDomainEntity.getFechaFin(),
            EstadoActividadResponse.fromEntity(actividadDomainEntity.getEstadoActividadDomainEntity()),
            actividadDomainEntity.getOrganizadorId(),
            TipoActividadResponse.fromEntity(actividadDomainEntity.getTipoActividadDomainEntity()),
            actividadDomainEntity.getLugar(),
            actividadDomainEntity.getCreatedAt(),
            actividadDomainEntity.getUpdatedAt(),
            actividadDomainEntity.isActive(),
            actividadDomainEntity.isFinished(),
            actividadDomainEntity.isPending(),
            actividadDomainEntity.getDurationInDays()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public EstadoActividadResponse getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividadResponse estado) {
        this.estado = estado;
    }

    public String getOrganizadorId() {
        return organizadorId;
    }
    public void setOrganizadorId(String organizadorId) {
        this.organizadorId = organizadorId;
    }

    public TipoActividadResponse getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(TipoActividadResponse tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }

    public long getDuracionEnDias() {
        return duracionEnDias;
    }

    public void setDuracionEnDias(long duracionEnDias) {
        this.duracionEnDias = duracionEnDias;
    }
}