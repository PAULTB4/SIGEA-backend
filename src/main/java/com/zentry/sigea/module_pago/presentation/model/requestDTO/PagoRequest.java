package com.zentry.sigea.module_pago.presentation.model.requestDTO;

import java.math.BigDecimal;

public class PagoRequest {

    private String titulo;
    private String descripcion;
    private BigDecimal monto;
    private String referencia;
    private String metodoPagoId;
    private String estadoPagoId;
    
    // Getters y setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(String metodoPagoId) { this.metodoPagoId = metodoPagoId; }
    public String getEstadoPagoId() { return estadoPagoId; }
    public void setEstadoPagoId(String estadoPagoId) { this.estadoPagoId = estadoPagoId; }
    
}
