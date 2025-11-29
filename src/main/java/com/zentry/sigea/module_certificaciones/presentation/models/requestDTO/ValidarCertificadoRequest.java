package com.zentry.sigea.module_certificaciones.presentation.models.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitud de validación de certificado
 */
public class ValidarCertificadoRequest {
    
    @NotBlank(message = "El código de validación es obligatorio")
    @Size(max = 50, message = "El código de validación no puede exceder 50 caracteres")
    private String codigoValidacion;
    
    @NotBlank(message = "El tipo de validador es obligatorio")
    private String tipoValidador; // QR | HASH | URL_PUBLICA | OCSP
    
    private String detalle;
    
    // Constructores
    public ValidarCertificadoRequest() {}
    
    public ValidarCertificadoRequest(String codigoValidacion, String tipoValidador) {
        this.codigoValidacion = codigoValidacion;
        this.tipoValidador = tipoValidador;
    }
    
    // Getters y Setters
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
    
    public String getDetalle() {
        return detalle;
    }
    
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}