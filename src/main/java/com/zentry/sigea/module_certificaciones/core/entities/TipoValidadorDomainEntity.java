package com.zentry.sigea.module_certificaciones.core.entities;

/**
 * Entidad de dominio para tipos de validador
 * Representa los diferentes tipos de validación que se pueden aplicar a un certificado
 */
public class TipoValidadorDomainEntity {
    private String codigo; // QR | HASH | URL_PUBLICA | OCSP
    private String etiqueta;

    // Constructors
    public TipoValidadorDomainEntity() {}

    public TipoValidadorDomainEntity(String codigo, String etiqueta) {
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
     * Factory method para crear tipo validador QR
     */
    public static TipoValidadorDomainEntity qr() {
        return new TipoValidadorDomainEntity("QR", "Validación por código QR");
    }

    /**
     * Factory method para crear tipo validador HASH
     */
    public static TipoValidadorDomainEntity hash() {
        return new TipoValidadorDomainEntity("HASH", "Validación por hash criptográfico");
    }

    /**
     * Factory method para crear tipo validador URL_PUBLICA
     */
    public static TipoValidadorDomainEntity urlPublica() {
        return new TipoValidadorDomainEntity("URL_PUBLICA", "Validación por URL pública");
    }

    /**
     * Factory method para crear tipo validador OCSP
     */
    public static TipoValidadorDomainEntity ocsp() {
        return new TipoValidadorDomainEntity("OCSP", "Validación por protocolo OCSP");
    }

    /**
     * Verifica si requiere conexión en línea
     */
    public boolean requiereConexion() {
        return "URL_PUBLICA".equals(this.codigo) || "OCSP".equals(this.codigo);
    }

    /**
     * Verifica si es un tipo de validación criptográfica
     */
    public boolean esCriptografico() {
        return "HASH".equals(this.codigo) || "OCSP".equals(this.codigo);
    }

    /**
     * Verifica si es válido para validación en tiempo real
     */
    public boolean esValidoTiempoReal() {
        return "QR".equals(this.codigo) || "URL_PUBLICA".equals(this.codigo);
    }

    /**
     * Verifica si es un tipo válido
     */
    public boolean esValido() {
        return codigo != null && 
               (codigo.equals("QR") || codigo.equals("HASH") || 
                codigo.equals("URL_PUBLICA") || codigo.equals("OCSP"));
    }

    /**
     * Obtiene la descripción del tipo de validación
     */
    public String getDescripcion() {
        switch (codigo) {
            case "QR":
                return "Validación mediante escaneo de código QR";
            case "HASH":
                return "Validación mediante verificación de hash criptográfico";
            case "URL_PUBLICA":
                return "Validación mediante consulta a URL pública";
            case "OCSP":
                return "Validación mediante protocolo OCSP (Online Certificate Status Protocol)";
            default:
                return etiqueta != null ? etiqueta : "Tipo de validación desconocido";
        }
    }
}