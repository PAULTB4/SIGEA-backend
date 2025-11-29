package com.zentry.sigea.module_notificaciones.events.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de dominio que se publica cuando se completa un pago
 * Este evento dispara automáticamente una notificación al usuario
 */
public class PagoCompletadoEvent {
    
    /**
     * Tipos de concepto de pago
     */
    public enum ConceptoPago {
        INSCRIPCION("Inscripción a actividad"),
        SESION("Pago por sesión individual"),
        CERTIFICADO("Emisión de certificado"),
        MATERIAL("Material del curso"),
        MEMBRESIA("Membresía o suscripción"),
        OTRO("Otro concepto");
        
        private final String descripcion;
        
        ConceptoPago(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    private final String usuarioId;
    private final String pagoId;
    private final BigDecimal monto;
    private final String moneda;
    private final String actividadId;
    private final String actividadTitulo;  // Nombre de la actividad
    private final ConceptoPago concepto;    // Por qué se paga
    private final String sesionTitulo;      // Nombre de la sesión (opcional, solo si concepto=SESION)
    private final String comprobante;
    private final String metodoPago;
    private final LocalDateTime fechaPago;
    
    public PagoCompletadoEvent(
        String usuarioId,
        String pagoId,
        BigDecimal monto,
        String moneda,
        String actividadId,
        String actividadTitulo,
        ConceptoPago concepto,
        String sesionTitulo,
        String comprobante,
        String metodoPago,
        LocalDateTime fechaPago
    ) {
        this.usuarioId = usuarioId;
        this.pagoId = pagoId;
        this.monto = monto;
        this.moneda = moneda;
        this.actividadId = actividadId;
        this.actividadTitulo = actividadTitulo;
        this.concepto = concepto;
        this.sesionTitulo = sesionTitulo;
        this.comprobante = comprobante;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
    }
    
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public String getPagoId() {
        return pagoId;
    }
    
    public BigDecimal getMonto() {
        return monto;
    }
    
    public String getMoneda() {
        return moneda;
    }
    
    public String getActividadId() {
        return actividadId;
    }
    
    public String getComprobante() {
        return comprobante;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public LocalDateTime getFechaPago() {
        return fechaPago;
    }
    
    public String getActividadTitulo() {
        return actividadTitulo;
    }
    
    public ConceptoPago getConcepto() {
        return concepto;
    }
    
    public String getSesionTitulo() {
        return sesionTitulo;
    }
}
