package com.zentry.sigea.module_certificaciones.presentation.models.responseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de certificado
 */
public class CertificadoResponse {
    
    private String idCertificado;
    private String asistenciaId;
    private String codigoValidacion;
    private LocalDate fechaEmision;
    private String estado;
    private String urlPdf;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Datos adicionales de la inscripción
    private String nombreUsuario;
    private String emailUsuario;
    private String tituloActividad;
    
    // Constructores
    public CertificadoResponse() {}
    
    public CertificadoResponse(String idCertificado, String asistenciaId, String codigoValidacion, 
                              LocalDate fechaEmision, String estado) {
        this.idCertificado = idCertificado;
        this.asistenciaId = asistenciaId;
        this.codigoValidacion = codigoValidacion;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
    }
    
    // Getters y Setters
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
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
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
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getEmailUsuario() {
        return emailUsuario;
    }
    
    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
    
    public String getTituloActividad() {
        return tituloActividad;
    }
    
    public void setTituloActividad(String tituloActividad) {
        this.tituloActividad = tituloActividad;
    }
}