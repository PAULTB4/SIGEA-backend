package com.zentry.sigea.module_certificaciones.presentation.models.responseDTO;

import java.time.LocalDate;

/**
 * DTO para respuesta de validación de certificado
 */
public class ValidacionResponse {
    
    private String  idValidacion;
    private String codigoValidacion;
    private String tipoValidador;
    private LocalDate fechaValidacion;
    private String resultado; // APROBADO | RECHAZADO
    private String detalle;
    
    // Información del certificado
    private CertificadoResponse certificado;
    
    // Constructores
    public ValidacionResponse() {}
    
    public ValidacionResponse(String codigoValidacion, String resultado) {
        this.codigoValidacion = codigoValidacion;
        this.resultado = resultado;
    }
    
    // Getters y Setters
    public String getIdValidacion() {
        return idValidacion;
    }
    
    public void setIdValidacion(String idValidacion) {
        this.idValidacion = idValidacion;
    }
    
    public String getCodigoValidacion() {
        return codigoValidacion;
    }
    
    public void setCodigoValidacion(String codigoValidacion) {
        this.codigoValidacion = codigoValidacion;
    }
    
    public String getTipoValidador() {
        return tipoValidador;
    }
    
    public void setTipoValidador(String tipoValidador) {
        this.tipoValidador = tipoValidador;
    }
    
    public LocalDate getFechaValidacion() {
        return fechaValidacion;
    }
    
    public void setFechaValidacion(LocalDate fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public String getDetalle() {
        return detalle;
    }
    
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    
    public CertificadoResponse getCertificado() {
        return certificado;
    }
    
    public void setCertificado(CertificadoResponse certificado) {
        this.certificado = certificado;
    }
}