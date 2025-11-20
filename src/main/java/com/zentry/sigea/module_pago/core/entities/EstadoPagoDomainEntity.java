package com.zentry.sigea.module_pago.core.entities;

public class EstadoPagoDomainEntity {
    private String idEstadoPago;
    private String descripcion;
    private String etiqueta;

    public String getIdEstadoPago() {
        return idEstadoPago;
    }
    public void setIdEstadoPago(String idEstadoPago) {
        this.idEstadoPago = idEstadoPago;
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

    // Factory method para crear una nueva instancia
    public static EstadoPagoDomainEntity create(String descripcion, String etiqueta) {
        EstadoPagoDomainEntity estadoPago = new EstadoPagoDomainEntity();
        estadoPago.setDescripcion(descripcion);
        estadoPago.setEtiqueta(etiqueta);
        return estadoPago;
    }
    
}
