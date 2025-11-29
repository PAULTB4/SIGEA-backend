package com.zentry.sigea.module_certificaciones.core.entities;

import java.time.LocalDate;

/**
 * Entidad de dominio para validaciones de certificado
 * Representa una validación realizada a un certificado
 */
public class ValidacionDomainEntity {
    private String certificado;
    private String tipoValidador;
    private LocalDate fechaValidacion;
    private String resultado; // APROBADO | RECHAZADO
    private String detalle;

    // Constructors
    public ValidacionDomainEntity() {}

    private ValidacionDomainEntity(String certificado, 
                                  String tipoValidador, 
                                  String resultado, String detalle) {
        this.certificado = certificado;
        this.tipoValidador = tipoValidador;
        this.resultado = resultado;
        this.detalle = detalle;
        this.fechaValidacion = LocalDate.now();
    }


    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
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

    /* MÉTODOS DEL DOMINIO */

    /**
     * Factory method para crear una validación aprobada
     */
    public static ValidacionDomainEntity crearAprobada(String certificado, 
                                                      String tipoValidador,
                                                      String detalle) {
        validateCreationParams(certificado, tipoValidador);
        return new ValidacionDomainEntity(certificado, tipoValidador, "APROBADO", detalle);
    }

    /**
     * Factory method para crear una validación rechazada
     */
    public static ValidacionDomainEntity crearRechazada(String certificado, 
                                                       String tipoValidador,
                                                       String detalle) {
        validateCreationParams(certificado, tipoValidador);
        if (detalle == null || detalle.trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle es obligatorio para validaciones rechazadas");
        }
        return new ValidacionDomainEntity(certificado, tipoValidador, "RECHAZADO", detalle);
    }

    /**
     * Actualiza el resultado de la validación
     */
    public void actualizarResultado(String nuevoResultado, String nuevoDetalle) {
        if (nuevoResultado == null || 
            (!nuevoResultado.equals("APROBADO") && !nuevoResultado.equals("RECHAZADO"))) {
            throw new IllegalArgumentException("El resultado debe ser APROBADO o RECHAZADO");
        }
        
        if ("RECHAZADO".equals(nuevoResultado) && 
            (nuevoDetalle == null || nuevoDetalle.trim().isEmpty())) {
            throw new IllegalArgumentException("El detalle es obligatorio para validaciones rechazadas");
        }
        
        this.resultado = nuevoResultado;
        this.detalle = nuevoDetalle;
        this.fechaValidacion = LocalDate.now(); // Actualizar fecha de validación
    }

    /**
     * Verifica si la validación fue aprobada
     */
    public boolean esAprobada() {
        return "APROBADO".equals(this.resultado);
    }

    /**
     * Verifica si la validación fue rechazada
     */
    public boolean esRechazada() {
        return "RECHAZADO".equals(this.resultado);
    }

    /**
     * Verifica si la validación es válida
     */
    public boolean esValida() {
        return resultado != null && 
               (resultado.equals("APROBADO") || resultado.equals("RECHAZADO")) &&
               certificado != null && 
               tipoValidador != null &&
               fechaValidacion != null;
    }

    private static void validateCreationParams(String certificado, 
                                             String tipoValidador) {
        if (certificado == null) {
            throw new IllegalArgumentException("El certificado es obligatorio");
        }
        if (tipoValidador == null) {
            throw new IllegalArgumentException("El tipo de validador es obligatorio");
        }
        if (tipoValidador.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de validador no puede estar vacío");
        }
    }
}