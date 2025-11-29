package com.zentry.sigea.module_notificaciones.infrastructure.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.ports.IWhatsAppService;

/**
 * Implementación del servicio de envío de mensajes por WhatsApp
 * Utiliza Twilio API para envío de mensajes
 */
@Service
public class WhatsAppServiceImpl implements IWhatsAppService {
    
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppServiceImpl.class);
    
    @Value("${notificaciones.whatsapp.account-sid:}")
    private String accountSid;
    
    @Value("${notificaciones.whatsapp.auth-token:}")
    private String authToken;
    
    @Value("${notificaciones.whatsapp.from-number:}")
    private String fromNumber;
    
    @Value("${notificaciones.whatsapp.enabled:false}")
    private boolean whatsappEnabled;
    
    private boolean twilioInitialized = false;

    @Override
    public boolean enviar(NotificacionDomainEntity notificacion, String telefono, String nombreDestinatario) {
        if (!whatsappEnabled) {
            logger.warn("Envío de WhatsApp deshabilitado en configuración. Mensaje no enviado a: {}", telefono);
            return false;
        }
        
        if (telefono == null || telefono.trim().isEmpty()) {
            logger.error("Número de teléfono vacío para notificación ID: {}", notificacion.getId());
            return false;
        }
        
        // Validar configuración de Twilio
        if (!validarConfiguracion()) {
            logger.error("Configuración de Twilio incompleta. Verifica application.properties");
            return false;
        }
        
        try {
            // Inicializar Twilio si no está inicializado
            if (!twilioInitialized) {
                Twilio.init(accountSid, authToken);
                twilioInitialized = true;
            }
            
            logger.info("Enviando WhatsApp a {} para notificación ID: {}", telefono, notificacion.getId());
            
            // Construir mensaje
            String contenido = construirMensaje(notificacion, nombreDestinatario);
            
            // Enviar mensaje usando Twilio
            Message message = Message.creator(
                new PhoneNumber("whatsapp:" + telefono),  // Destinatario con prefijo whatsapp:
                new PhoneNumber("whatsapp:" + fromNumber), // Remitente con prefijo whatsapp:
                contenido
            ).create();
            
            logger.info("WhatsApp enviado exitosamente. SID: {} a {} para notificación ID: {}", 
                message.getSid(), telefono, notificacion.getId());
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error al enviar WhatsApp a {} para notificación ID {}: {}", 
                telefono, notificacion.getId(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Valida que la configuración de Twilio esté completa
     */
    private boolean validarConfiguracion() {
        if (accountSid == null || accountSid.trim().isEmpty()) {
            logger.error("ACCOUNT_SID de Twilio no configurado");
            return false;
        }
        
        if (authToken == null || authToken.trim().isEmpty()) {
            logger.error("AUTH_TOKEN de Twilio no configurado");
            return false;
        }
        
        if (fromNumber == null || fromNumber.trim().isEmpty()) {
            logger.error("FROM_NUMBER de Twilio no configurado");
            return false;
        }
        
        return true;
    }
    
    /**
     * Construye el contenido del mensaje de WhatsApp
     */
    private String construirMensaje(NotificacionDomainEntity notificacion, String nombreDestinatario) {
        StringBuilder mensaje = new StringBuilder();
        
        // Emoji de campana para notificaciones
        mensaje.append("🔔 *SIGEA - Notificación*\n\n");
        
        // Saludo personalizado
        if (nombreDestinatario != null && !nombreDestinatario.trim().isEmpty()) {
            mensaje.append("Hola ").append(nombreDestinatario).append(",\n\n");
        }
        
        // Tipo de notificación
        if (notificacion.getTipoNotificacion() != null && 
            notificacion.getTipoNotificacion().getEtiqueta() != null) {
            mensaje.append("*Tipo:* ").append(notificacion.getTipoNotificacion().getEtiqueta()).append("\n\n");
        }
        
        // Mensaje principal
        mensaje.append(notificacion.getMensaje()).append("\n\n");
        
        // Fecha
        mensaje.append("📅 *Fecha:* ").append(notificacion.getFechaEnvio().toString()).append("\n\n");
        
        // Footer
        mensaje.append("---\n");
        mensaje.append("_SIGEA - Sistema de Gestión de Eventos Académicos_\n");
        mensaje.append("_Este es un mensaje automático_");
        
        return mensaje.toString();
    }
}
