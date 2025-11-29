package com.zentry.sigea.module_notificaciones.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

/**
 * Listener que escucha eventos de certificados y genera notificaciones automáticas
 * Principio: Single Responsibility - Solo maneja notificaciones de certificados
 */
@Component
public class CertificadoEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(CertificadoEventListener.class);
    private static final String TIPO_NOTIFICACION_CERTIFICADO = "CERTIFICADO";
    
    private final NotificacionService notificacionService;
    
    public CertificadoEventListener(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }
    
    /**
     * Escucha el evento de certificado generado y genera notificación automática
     * @Async permite que la notificación se procese en segundo plano sin bloquear la generación
     */
    @EventListener
    @Async
    public void onCertificadoGenerado(CertificadoGeneradoEvent event) {
        try {
            logger.info("🎓 Evento recibido: Certificado generado {} para usuario {} - Código: {}", 
                event.getCertificadoId(), event.getUsuarioId(), event.getCodigoValidacion());
            
            // Formatear fecha de emisión
            String fechaFormateada = event.getFechaEmision()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            // Construir mensaje profesional con todos los detalles del certificado
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("🎓 ¡Felicidades! Tu certificado está listo\n\n");
            mensaje.append("══════════════════════════════════════════\n\n");
            mensaje.append(String.format("🎯 Actividad: %s\n", event.getActividadTitulo()));
            mensaje.append(String.format("📋 Estado: %s\n", event.getEstado().getDescripcion()));
            mensaje.append(String.format("📅 Fecha de emisión: %s\n\n", fechaFormateada));
            mensaje.append("🔐 Código de validación:\n");
            mensaje.append(String.format("   %s\n\n", event.getCodigoValidacion()));
            
            // Agregar URL de descarga si está disponible
            if (event.getUrlPdf() != null && !event.getUrlPdf().isEmpty()) {
                mensaje.append("📥 Descarga tu certificado:\n");
                mensaje.append(String.format("   %s\n\n", event.getUrlPdf()));
            } else {
                mensaje.append("📥 Tu certificado estará disponible para descarga pronto.\n\n");
            }
            
            mensaje.append("══════════════════════════════════════════\n\n");
            mensaje.append("💡 Puedes validar tu certificado usando el código de validación.\n");
            mensaje.append("📌 Guarda este código para futuras referencias.\n\n");
            mensaje.append("¡Felicitaciones por tu logro! 🎉");
            
            // Constructor: usuarioId, actividadId, tipoNotificacionId, mensaje, estadoNotificacionId, canal
            CrearNotificacionRequest notificacionRequest = new CrearNotificacionRequest(
                event.getUsuarioId(),
                event.getActividadId(),
                TIPO_NOTIFICACION_CERTIFICADO,
                mensaje.toString(),
                null, // estadoNotificacionId (el service lo asigna)
                "SISTEMA"
            );
            
            String resultado = notificacionService.crearNotificacion(notificacionRequest);
            
            logger.info("Notificación de certificado enviada exitosamente - Usuario: {}, Código: {}", 
                event.getUsuarioId(), event.getCodigoValidacion());
            
        } catch (Exception e) {
            logger.error("Error al procesar notificación de certificado para usuario {}: {}", 
                event.getUsuarioId(), e.getMessage(), e);
            // No lanzamos la excepción para no afectar el flujo principal
        }
    }
}
