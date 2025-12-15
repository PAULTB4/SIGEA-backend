package com.zentry.sigea.module_pago.presentation.models.requestDTO;

import java.math.BigDecimal;

public class YapePaymentRequest {
    private String usuarioId;
    private String descripcion;
    private BigDecimal monto;
    
    // Getters y Setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    
}