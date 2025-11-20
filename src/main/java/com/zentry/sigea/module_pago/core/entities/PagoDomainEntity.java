package com.zentry.sigea.module_pago.core.entities;

public class PagoDomainEntity {
    private String idPago;
    private String inscripcionId;
    private Double monto;
    private String moneda;
    private String fechaPago;
    private String comprobante;
    private String metodoId;
    private String estadoId;
    private String referenciaExt;

    // Getters and Setters
    public String getIdPago() {
        return idPago;
    }
    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }
    public String getInscripcionId() {
        return inscripcionId;
    }
    public void setInscripcionId(String inscripcionId) {
        this.inscripcionId = inscripcionId;
    }
    public Double getMonto() {
        return monto;
    }
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public String getFechaPago() {
        return fechaPago;
    }
    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
    public String getComprobante() {
        return comprobante;
    }
    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getMetodoId() {
        return metodoId;
    }
    public void setMetodoId(String metodoId) {
        this.metodoId = metodoId;
    }
    public String getEstadoId() {
        return estadoId;
    }
    public void setEstadoId(String estadoId) {
        this.estadoId = estadoId;
    }
    public String getReferenciaExt() {
        return referenciaExt;
    }
    public void setReferenciaExt(String referenciaExt) {
        this.referenciaExt = referenciaExt;
    }

    public static PagoDomainEntity create(
        String inscripcionId,
        Double monto,
        String moneda,
        String fechaPago,
        String comprobante,
        String metodoId,
        String estadoId,
        String referenciaExt
    ) {
        PagoDomainEntity pago = new PagoDomainEntity();
        pago.setInscripcionId(inscripcionId);
        pago.setMonto(monto);
        pago.setMoneda(moneda);
        pago.setFechaPago(fechaPago);
        pago.setComprobante(comprobante);
        pago.setMetodoId(metodoId);
        pago.setEstadoId(estadoId);
        pago.setReferenciaExt(referenciaExt);
        return pago;
    }




    
}
