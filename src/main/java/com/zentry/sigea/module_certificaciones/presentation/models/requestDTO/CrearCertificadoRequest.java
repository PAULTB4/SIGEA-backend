package com.zentry.sigea.module_certificaciones.presentation.models.requestDTO;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para solicitud de creación de certificado
 */
public class CrearCertificadoRequest {

    @NotNull(message = "El ID de inscripción es obligatorio")
    private String asistenciaId;

    private String observaciones;

    /**
     * Tipo de certificado: GENERADO (automático) o SUBIDO (archivo)
     */
    @NotNull(message = "El tipo de certificado es obligatorio")
    private String tipoCertificado; // "GENERADO" o "SUBIDO"

    /**
     * Nombre del archivo (opcional, solo si es SUBIDO)
     */
    private String nombreArchivo;

    // Constructores
    public CrearCertificadoRequest() {}

    public CrearCertificadoRequest(String asistenciaId, String tipoCertificado) {
        this.asistenciaId = asistenciaId;
        this.tipoCertificado = tipoCertificado;
    }

    // Getters y Setters
    public String getAsistenciaId() {
        return asistenciaId;
    }

    public void setAsistenciaId(String asistenciaId) {
        this.asistenciaId = asistenciaId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(String tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}