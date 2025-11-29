package com.zentry.sigea.module_notificaciones.events.listeners;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

/**
 * Listener que escucha eventos de pagos y genera notificaciones automáticas
 * Principio: Single Responsibility - Solo maneja notificaciones de pagos
 */
@Component
public class PagoEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(PagoEventListener.class);
    private static final String TIPO_NOTIFICACION_PAGO = "PAGO";
    private static final DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
    
    private final NotificacionService notificacionService;
    
    public PagoEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Escucha el evento de pago completado y genera notificación automática
     * @Async permite que la notificación se procese en segundo plano sin bloquear el pago
     */
    @EventListener
    @Async
    public void onPagoCompletado(PagoCompletadoEvent event) {
        try {
            logger.info("💰 Evento recibido: Pago completado - Usuario: {}, Monto: {} {}", 
                event.getUsuarioId(), event.getMonto(), event.getMoneda());
            
            // Formatear el monto
            String montoFormateado = formatoMoneda.format(event.getMonto());
            String simboloMoneda = obtenerSimboloMoneda(event.getMoneda());
            
            // Construir mensaje detallado y profesional
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("💳 ¡Pago recibido exitosamente!\n\n");
            mensaje.append("══════════════════════════════════════════\n\n");
            
            // Concepto del pago
            mensaje.append(String.format("📋 Concepto: %s\n", event.getConcepto().getDescripcion()));
            
            // Información de actividad
            if (event.getActividadTitulo() != null && !event.getActividadTitulo().isEmpty()) {
                mensaje.append(String.format("🎯 Actividad: %s\n", event.getActividadTitulo()));
            }
            
            // Información de sesión (solo si el pago es por sesión)
            if (event.getConcepto() == PagoCompletadoEvent.ConceptoPago.SESION && 
                event.getSesionTitulo() != null && !event.getSesionTitulo().isEmpty()) {
                mensaje.append(String.format("📅 Sesión: %s\n", event.getSesionTitulo()));
            }
            
            mensaje.append("\n📊 Detalles de la transacción:\n");
            mensaje.append("──────────────────────────────────────────\n");
            mensaje.append(String.format("💰 Monto pagado: %s %s\n", simboloMoneda, montoFormateado));
            
            if (event.getMetodoPago() != null && !event.getMetodoPago().isEmpty()) {
                mensaje.append(String.format("💳 Método de pago: %s\n", event.getMetodoPago()));
            }
            
            if (event.getComprobante() != null && !event.getComprobante().isEmpty()) {
                mensaje.append(String.format("📄 Nº Comprobante: %s\n", event.getComprobante()));
            }
            
            mensaje.append(String.format("📅 Fecha y hora: %s\n", 
                event.getFechaPago().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            mensaje.append("──────────────────────────────────────────\n\n");
            
            mensaje.append("✅ Tu transacción ha sido procesada correctamente.\n");
            mensaje.append("📧 Conserva este comprobante para futuras referencias.");
            
            // Constructor: usuarioId, actividadId, tipoNotificacionId, mensaje, estadoNotificacionId, canal
            CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                event.getUsuarioId(),
                event.getActividadId(),
                TIPO_NOTIFICACION_PAGO,
                mensaje.toString(),
                null, // estadoNotificacionId (el service lo asigna)
                "SISTEMA"
            );
            
            String resultado = notificacionService.crearNotificacion(notificacionRequest);
            logger.info("✅ Notificación de pago enviada a usuario {}: {}", event.getUsuarioId(), resultado);
            
        } catch (Exception e) {
            logger.error("❌ Error al procesar notificación de pago para usuario {}: {}", 
                event.getUsuarioId(), e.getMessage(), e);
            // No lanzamos la excepción para no afectar el flujo principal de pago
        }
    }
    
    /**
     * Obtiene el símbolo de la moneda
     */
    private String obtenerSimboloMoneda(String codigoMoneda) {
        return switch (codigoMoneda) {
            case "PEN" -> "S/.";
            case "USD" -> "$";
            case "EUR" -> "€";
            default -> codigoMoneda;
        };
    }
}
