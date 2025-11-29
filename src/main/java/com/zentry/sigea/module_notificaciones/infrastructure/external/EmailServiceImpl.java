package com.zentry.sigea.module_notificaciones.infrastructure.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.ports.IEmailService;

import jakarta.mail.internet.MimeMessage;

/**
 * Implementación del servicio de envío de emails
 * Utiliza JavaMailSender de Spring Boot
 */
@Service
public class EmailServiceImpl implements IEmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    private final JavaMailSender mailSender;
    
    @Value("${notificaciones.email.from:noreply@sigea.com}")
    private String emailFrom;
    
    @Value("${notificaciones.email.enabled:false}")
    private boolean emailEnabled;
    
    @Value("${notificaciones.email.nombre-remitente:SIGEA - Sistema de Gestión}")
    private String nombreRemitente;
    
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean enviar(NotificacionDomainEntity notificacion, String destinatario, String nombreDestinatario) {
        logger.info("🔵 EmailService.enviar() INICIADO - emailEnabled: {}", emailEnabled);
        logger.info("🔵 Destinatario: {}, Nombre: {}", destinatario, nombreDestinatario);
        
        if (!emailEnabled) {
            logger.warn("⚠️ Envío de emails DESHABILITADO en configuración. Email no enviado a: {}", destinatario);
            return false;
        }
        
        if (destinatario == null || destinatario.trim().isEmpty()) {
            logger.error("❌ Destinatario de email vacío para notificación ID: {}", notificacion.getId());
            return false;
        }
        
        try {
            logger.info("📧 Preparando envío de email a {} para notificación ID: {}", destinatario, notificacion.getId());
            logger.info("📧 Email FROM configurado: {}", emailFrom);
            
            // Crear mensaje HTML para mejor presentación
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // Configurar remitente con codificación UTF-8 explícita
            helper.setFrom(emailFrom, nombreRemitente);
            helper.setTo(destinatario);
            
            // Asunto con codificación UTF-8
            String asunto = obtenerAsunto(notificacion);
            helper.setSubject(asunto);
            
            // Contenido HTML con codificación UTF-8
            helper.setText(construirContenidoHtml(notificacion, nombreDestinatario), true);
            
            logger.info("📤 Llamando a mailSender.send()...");
            mailSender.send(message);
            
            logger.info("✅✅✅ Email enviado EXITOSAMENTE a {} para notificación ID: {}", 
                destinatario, notificacion.getId());
            return true;
            
        } catch (Exception e) {
            logger.error("💥💥💥 ERROR CRÍTICO al enviar email a {} para notificación ID {}", 
                destinatario, notificacion.getId());
            logger.error("💥 Tipo de excepción: {}", e.getClass().getName());
            logger.error("💥 Mensaje: {}", e.getMessage());
            logger.error("💥 Stack trace completo:", e);
            return false;
        }
    }
    
    /**
     * Construye el asunto del email basado en el tipo de notificación
     */
    private String obtenerAsunto(NotificacionDomainEntity notificacion) {
        if (notificacion.getTipoNotificacion() == null) {
            return "SIGEA - Nueva Notificacion";
        }
        
        String codigo = notificacion.getTipoNotificacion().getCodigo();
        
        // Asuntos específicos según el tipo
        switch (codigo) {
            case "INSCRIPCION":
                return "SIGEA - Inscripcion Confirmada";
            case "COMUNICACION":
                // Para comunicaciones, extraer si es actividad o sesión del mensaje
                String mensaje = notificacion.getMensaje();
                if (mensaje.contains("Nueva actividad:") || mensaje.contains("nueva actividad")) {
                    return "SIGEA - Nueva Actividad";
                } else if (mensaje.contains("sesion") || mensaje.contains("Sesion")) {
                    return "SIGEA - Nueva Sesion Programada";
                }
                return "SIGEA - Comunicacion";
            case "PAGO":
                return "SIGEA - Pago Recibido";
            case "CERTIFICADO":
                return "SIGEA - Tu Certificado esta Listo";
            default:
                return "SIGEA - Notificacion";
        }
    }
    
    /**
     * Construye el contenido HTML del email
     */
    private String construirContenidoHtml(NotificacionDomainEntity notificacion, String nombreDestinatario) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f5f5f5; }");
        html.append(".header { background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%); color: white; padding: 30px 20px; text-align: center; border-radius: 8px 8px 0 0; }");
        html.append(".header h1 { margin: 0; font-size: 24px; }");
        html.append(".content { background-color: white; padding: 30px; border-radius: 0 0 8px 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        html.append(".greeting { font-size: 16px; margin-bottom: 20px; color: #333; }");
        html.append(".message-box { background-color: #f9f9f9; padding: 20px; border-left: 4px solid #4CAF50; margin: 20px 0; border-radius: 4px; }");
        html.append(".message-box p { margin: 0; font-size: 15px; line-height: 1.6; }");
        html.append(".footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd; font-size: 12px; color: #666; }");
        html.append(".btn { display: inline-block; padding: 12px 24px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<h1>SIGEA</h1>");
        html.append("<p style='margin: 5px 0 0 0; font-size: 14px;'>Sistema de Gestion de Eventos Academicos</p>");
        html.append("</div>");
        
        // Content
        html.append("<div class='content'>");
        
        // Saludo personalizado
        String saludo = nombreDestinatario != null && !nombreDestinatario.trim().isEmpty() 
            ? "Hola " + nombreDestinatario 
            : "Hola";
        html.append("<p class='greeting'><strong>").append(saludo).append(",</strong></p>");
        
        // Mensaje principal (limpio, sin decoraciones extras)
        html.append("<div class='message-box'>");
        html.append("<p>").append(notificacion.getMensaje()).append("</p>");
        html.append("</div>");
        
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p style='margin: 5px 0;'>Este es un mensaje automatico del sistema SIGEA.</p>");
        html.append("<p style='margin: 5px 0;'>Por favor no responder a este correo.</p>");
        html.append("<p style='margin: 15px 0 0 0; color: #999;'>&copy; 2025 SIGEA - Todos los derechos reservados</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Método alternativo usando SimpleMailMessage (texto plano)
     * Útil como fallback si falla el envío HTML
     */
    @SuppressWarnings("unused")
    private void enviarTextoPlano(NotificacionDomainEntity notificacion, 
                                   String destinatario, 
                                   String nombreDestinatario) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(destinatario);
        message.setSubject(obtenerAsunto(notificacion));
        message.setText(construirContenidoTexto(notificacion, nombreDestinatario));
        
        mailSender.send(message);
    }
    
    /**
     * Construye el contenido en texto plano
     */
    private String construirContenidoTexto(NotificacionDomainEntity notificacion, String nombreDestinatario) {
        StringBuilder texto = new StringBuilder();
        
        String saludo = nombreDestinatario != null && !nombreDestinatario.trim().isEmpty() 
            ? "Hola " + nombreDestinatario + "," 
            : "Hola,";
        
        texto.append(saludo).append("\n\n");
        
        if (notificacion.getTipoNotificacion() != null) {
            texto.append("Tipo: ").append(notificacion.getTipoNotificacion().getEtiqueta()).append("\n\n");
        }
        
        texto.append(notificacion.getMensaje()).append("\n\n");
        texto.append("Fecha: ").append(notificacion.getFechaEnvio()).append("\n\n");
        texto.append("---\n");
        texto.append("SIGEA - Sistema de Gestión de Eventos Académicos\n");
        texto.append("Este es un mensaje automático, por favor no responder.");
        
        return texto.toString();
    }
}
