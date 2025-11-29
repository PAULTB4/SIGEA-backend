package com.zentry.sigea.module_notificaciones.events.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento de dominio que se publica cuando se genera un certificado
 * Este evento dispara automáticamente una notificación al usuario
 * 
 * Basado en la estructura de CertificadoEntity con todos los atributos relevantes
 */
public class CertificadoGeneradoEvent {
    
    /**
     * Estados posibles del certificado (basado en EstadoCertificadoEntity)
     */
    public enum EstadoCertificado {
        GENERADO("Certificado generado"),
        EMITIDO("Certificado emitido"),
        VALIDADO("Certificado validado"),
        ANULADO("Certificado anulado");
        
        private final String descripcion;
        
        EstadoCertificado(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    private final String usuarioId;              // ID del usuario que recibe el certificado
    private final String certificadoId;          // ID del certificado generado
    private final String actividadId;            // ID de la actividad completada
    private final String actividadTitulo;        // Título de la actividad
    private final String codigoValidacion;       // Código único para validar el certificado (max 50 caracteres)
    private final LocalDate fechaEmision;        // Fecha de emisión del certificado
    private final EstadoCertificado estado;      // Estado del certificado
    private final String urlPdf;                 // URL donde se puede descargar el PDF (max 300 caracteres)
    private final String asistenciaId;           // ID de la asistencia relacionada
    private final LocalDateTime fechaGeneracion; // Fecha y hora de generación
    
    public CertificadoGeneradoEvent(
            String usuarioId,
            String certificadoId,
            String actividadId,
            String actividadTitulo,
            String codigoValidacion,
            LocalDate fechaEmision,
            EstadoCertificado estado,
            String urlPdf,
            String asistenciaId,
            LocalDateTime fechaGeneracion) {
        this.usuarioId = usuarioId;
        this.certificadoId = certificadoId;
        this.actividadId = actividadId;
        this.actividadTitulo = actividadTitulo;
        this.codigoValidacion = codigoValidacion;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.urlPdf = urlPdf;
        this.asistenciaId = asistenciaId;
        this.fechaGeneracion = fechaGeneracion;
    }
    
    // Getters
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public String getCertificadoId() {
        return certificadoId;
    }
    
    public String getActividadId() {
        return actividadId;
    }
    
    public String getActividadTitulo() {
        return actividadTitulo;
    }
    
    public String getCodigoValidacion() {
        return codigoValidacion;
    }
    
    public LocalDate getFechaEmision() {
        return fechaEmision;
    }
    
    public EstadoCertificado getEstado() {
        return estado;
    }
    
    public String getUrlPdf() {
        return urlPdf;
    }
    
    public String getAsistenciaId() {
        return asistenciaId;
    }
    
    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }
}
