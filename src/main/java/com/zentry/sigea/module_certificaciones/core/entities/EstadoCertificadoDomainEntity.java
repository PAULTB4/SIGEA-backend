package com.zentry.sigea.module_certificaciones.core.entities;

/**
 * Entidad de dominio para estados de certificado
 * Representa los posibles estados que puede tener un certificado
 */
public class EstadoCertificadoDomainEntity {
    private String codigo; // EMITIDO | REVOCADO | SUSPENDIDO
    private String etiqueta;

    // Constructors
    public EstadoCertificadoDomainEntity() {}

    public EstadoCertificadoDomainEntity(String codigo, String etiqueta) {
        this.codigo = codigo;
        this.etiqueta = etiqueta;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /* MÉTODOS DEL DOMINIO */

    /**
     * Factory method para crear estado PENDIENTE
     */
    public static EstadoCertificadoDomainEntity pendiente() {
        return new EstadoCertificadoDomainEntity("PENDIENTE", "Pendiente");
    }

    /**
     * Factory method para crear estado EMITIDO
     */
    public static EstadoCertificadoDomainEntity emitido() {
        return new EstadoCertificadoDomainEntity("EMITIDO", "Emitido");
    }

    /**
     * Factory method para crear estado REVOCADO
     */
    public static EstadoCertificadoDomainEntity revocado() {
        return new EstadoCertificadoDomainEntity("REVOCADO", "Revocado");
    }

    /**
     * Factory method para crear estado SUSPENDIDO
     */
    public static EstadoCertificadoDomainEntity suspendido() {
        return new EstadoCertificadoDomainEntity("SUSPENDIDO", "Suspendido");
    }

    /**
     * Verifica si el estado permite emisión de certificados
     */
    public boolean permiteEmision() {
        return "EMITIDO".equals(this.codigo);
    }

    /**
     * Verifica si el estado es final (no permite cambios)
     */
    public boolean esFinal() {
        return "REVOCADO".equals(this.codigo);
    }

    /**
     * Verifica si es un estado válido
     */
    public boolean esValido() {
         return codigo != null && 
             (codigo.equals("EMITIDO") || codigo.equals("REVOCADO") || codigo.equals("SUSPENDIDO") || codigo.equals("PENDIENTE"));
    }
}