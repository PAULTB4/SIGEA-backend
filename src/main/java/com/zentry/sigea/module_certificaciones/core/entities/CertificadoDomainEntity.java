package com.zentry.sigea.module_certificaciones.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio para certificados
 * Representa un certificado emitido para una inscripción completada
 */
public class CertificadoDomainEntity {
    private String idCertificado;
    private String asistenciaId;
    private String codigoValidacion;
    private LocalDate fechaEmision;
    private EstadoCertificadoDomainEntity estado;
    private String urlPdf;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CertificadoDomainEntity() {}

    private CertificadoDomainEntity(String asistenciaId, String codigoValidacion, 
                                   EstadoCertificadoDomainEntity estado) {
        LocalDateTime now = LocalDateTime.now();
        this.asistenciaId = asistenciaId;
        this.codigoValidacion = codigoValidacion;
        this.fechaEmision = LocalDate.now();
        this.estado = estado;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public String getIdCertificado() {
        return idCertificado;
    }
    public void setIdCertificado(String idCertificado) {
        this.idCertificado = idCertificado;
    }


    public String getAsistenciaId() {
        return asistenciaId;
    }

    public void setAsistenciaId(String asistenciaId) {
        this.asistenciaId = asistenciaId;
    }

    public String getCodigoValidacion() {
        return codigoValidacion;
    }

    public void setCodigoValidacion(String codigoValidacion) {
        this.codigoValidacion = codigoValidacion;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public EstadoCertificadoDomainEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoCertificadoDomainEntity estado) {
        this.estado = estado;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
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

    /* MÉTODOS DEL DOMINIO */

    /**
     * Factory method para crear un nuevo certificado
     */
    public static CertificadoDomainEntity create(String inscripcionId, String codigoValidacion, 
                                               EstadoCertificadoDomainEntity estado) {
        if (inscripcionId == null || inscripcionId.trim().isEmpty()) {
            throw new IllegalArgumentException("La inscripción ID es obligatoria");
        }
        if (codigoValidacion == null || codigoValidacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de validación es obligatorio");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        return new CertificadoDomainEntity(inscripcionId, codigoValidacion, estado);
    }

    /**
     * Cambia el estado del certificado
     */
    public void cambiarEstado(EstadoCertificadoDomainEntity nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Validaciones de negocio para cambios de estado
        if (this.estado != null && this.estado.getCodigo().equals("REVOCADO") 
            && nuevoEstado.getCodigo().equals("EMITIDO")) {
            // Permitir reactivación desde revocado
        } else if (this.estado != null && this.estado.getCodigo().equals("REVOCADO")) {
            throw new IllegalStateException("No se puede cambiar el estado de un certificado revocado");
        }
        
        this.estado = nuevoEstado;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Revoca el certificado
     */
    public void revocar(EstadoCertificadoDomainEntity estadoRevocado) {
        if (estadoRevocado == null || !"REVOCADO".equals(estadoRevocado.getCodigo())) {
            throw new IllegalArgumentException("El estado debe ser REVOCADO");
        }
        
        if (this.estado != null && "REVOCADO".equals(this.estado.getCodigo())) {
            throw new IllegalStateException("El certificado ya está revocado");
        }
        
        cambiarEstado(estadoRevocado);
    }

    /**
     * Reactiva el certificado
     */
    public void reactivar(EstadoCertificadoDomainEntity estadoEmitido) {
        if (estadoEmitido == null || !"EMITIDO".equals(estadoEmitido.getCodigo())) {
            throw new IllegalArgumentException("El estado debe ser EMITIDO");
        }
        
        if (this.estado == null || !"REVOCADO".equals(this.estado.getCodigo())) {
            throw new IllegalStateException("Solo se pueden reactivar certificados revocados");
        }
        
        cambiarEstado(estadoEmitido);
    }

    /**
     * Establece la URL del PDF generado
     */
    public void establecerUrlPdf(String urlPdf) {
        if (urlPdf == null || urlPdf.trim().isEmpty()) {
            throw new IllegalArgumentException("La URL del PDF no puede estar vacía");
        }
        this.urlPdf = urlPdf;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si el certificado está emitido
     */
    public boolean estaEmitido() {
        return estado != null && "EMITIDO".equals(estado.getCodigo());
    }

    /**
     * Verifica si el certificado está revocado
     */
    public boolean estaRevocado() {
        return estado != null && "REVOCADO".equals(estado.getCodigo());
    }

    /**
     * Verifica si el certificado está suspendido
     */
    public boolean estaSuspendido() {
        return estado != null && "SUSPENDIDO".equals(estado.getCodigo());
    }

    /**
     * Verifica si el certificado es válido (emitido y no revocado)
     */
    public boolean esValido() {
        return estaEmitido();
    }
}