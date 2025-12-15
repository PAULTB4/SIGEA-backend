package com.zentry.sigea.module_pago.presentation.models.requestDTO;

public class MetodoPagoRequest {
    private String descripcion;
    private String etiqueta;

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
