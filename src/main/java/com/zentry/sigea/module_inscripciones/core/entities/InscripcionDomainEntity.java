package com.zentry.sigea.module_inscripciones.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio para inscripciones
 * Representa la relación entre un usuario y una actividad
 */
public class InscripcionDomainEntity {
    private String idInscripcion;
    private LocalDate fechaInscripcion;
    private String usuarioId;
    private String actividadId;
    private EstadoInscripcionDomainEntity estadoInscripcionDomainEntity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(String idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }
    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getActividadId() {
        return actividadId;
    }
    public void setActividadId(String actividadId) {
        this.actividadId = actividadId;
    }

    public EstadoInscripcionDomainEntity getEstadoInscripcionDomainEntity() {
        return estadoInscripcionDomainEntity;
    }
    public void setEstadoInscripcionDomainEntity(EstadoInscripcionDomainEntity estadoInscripcionDomainEntity) {
        this.estadoInscripcionDomainEntity = estadoInscripcionDomainEntity;
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

    /**
     * Factory method para crear una nueva inscripción
     */
    public static InscripcionDomainEntity create(
        LocalDate fechaInscripcion,
        String usuarioId,
        String actividadId,
        EstadoInscripcionDomainEntity estadoInscripcionDomainEntity
    ) {
        LocalDateTime now = LocalDateTime.now();

        InscripcionDomainEntity inscripcionDomainEntity = new InscripcionDomainEntity();

        inscripcionDomainEntity.setFechaInscripcion(fechaInscripcion != null ? fechaInscripcion : LocalDate.now());
        inscripcionDomainEntity.setUsuarioId(usuarioId);
        inscripcionDomainEntity.setActividadId(actividadId);
        inscripcionDomainEntity.setEstadoInscripcionDomainEntity(estadoInscripcionDomainEntity);
        inscripcionDomainEntity.setCreatedAt(now);
        inscripcionDomainEntity.setUpdatedAt(now);

        return inscripcionDomainEntity;
    }

    /**
     * Cambia el estado de la inscripción
     */
    public void cambiarEstado(EstadoInscripcionDomainEntity nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estadoInscripcionDomainEntity = nuevoEstado;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si la inscripción está confirmada
     */
    public boolean estaConfirmada() {
        return estadoInscripcionDomainEntity != null && 
               "CONFIRMADA".equals(estadoInscripcionDomainEntity.getCodigo());
    }

    /**
     * Verifica si la inscripción está cancelada
     */
    public boolean estaCancelada() {
        return estadoInscripcionDomainEntity != null && 
               "CANCELADA".equals(estadoInscripcionDomainEntity.getCodigo());
    }

    /**
     * Verifica si la inscripción está pendiente
     */
    public boolean estaPendiente() {
        return estadoInscripcionDomainEntity != null && 
               "PENDIENTE".equals(estadoInscripcionDomainEntity.getCodigo());
    }
}

