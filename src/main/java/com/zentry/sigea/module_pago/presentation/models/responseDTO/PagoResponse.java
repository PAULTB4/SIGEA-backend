package com.zentry.sigea.module_pago.presentation.models.responseDTO;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class PagoResponse {
    private String preferenceId;
    private String initPoint;
    private String sandboxInitPoint;

    // Fields from PagoDomainEntity
    private String idPago;
    private String inscripcionId;
    private BigDecimal monto;
    private String moneda;
    private OffsetDateTime fechaPago;
    private String comprobante;
    private String metodoId;
    private String estadoId;
    private String referenciaExt;
    private String usuarioId;
    private String actividadId;
    private String usuarioDni;

    public PagoResponse() {
    }

    public PagoResponse(PagoDomainEntity entity) {
        this.idPago = entity.getIdPago();
        this.inscripcionId = entity.getInscripcionId();
        this.monto = entity.getMonto();
        this.moneda = entity.getMoneda();
        this.fechaPago = entity.getFechaPago();
        this.comprobante = entity.getComprobante();
        this.metodoId = entity.getMetodoId();
        this.estadoId = entity.getEstadoId();
        this.referenciaExt = entity.getReferenciaExt();
        this.usuarioId = entity.getUsuarioId();
        this.actividadId = entity.getActividadId();
        this.usuarioDni = entity.getUsuarioDni();
    }

    public static PagoResponseBuilder builder() {
        return new PagoResponseBuilder();
    }

    // Getters y setters
    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }

    public String getSandboxInitPoint() {
        return sandboxInitPoint;
    }

    public void setSandboxInitPoint(String sandboxInitPoint) {
        this.sandboxInitPoint = sandboxInitPoint;
    }

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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public OffsetDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(OffsetDateTime fechaPago) {
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

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getActividadId() {
        return actividadId;
    }

    public void setActividadId(String actividadId) {
        this.actividadId = actividadId;
    }

    public String getUsuarioDni() {
        return usuarioDni;
    }

    public void setUsuarioDni(String usuarioDni) {
        this.usuarioDni = usuarioDni;
    }

    static class PagoResponseBuilder {
        private PagoResponse response = new PagoResponse();

        public PagoResponseBuilder preferenceId(String id) {
            response.preferenceId = id;
            return this;
        }

        public PagoResponseBuilder initPoint(String url) {
            response.initPoint = url;
            return this;
        }

        public PagoResponseBuilder sandboxInitPoint(String url) {
            response.sandboxInitPoint = url;
            return this;
        }

        public PagoResponse build() {
            return response;
        }
    }
}
