package com.zentry.sigea.module_pago.core.entities;

public class MetodoPagoDomainEntity {
    private String idMetodoPago;
    private String descripcion;
    private String etiqueta;

    public String getIdMetodoPago() {
        return idMetodoPago;
    }
    public void setIdMetodoPago(String idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    
}
