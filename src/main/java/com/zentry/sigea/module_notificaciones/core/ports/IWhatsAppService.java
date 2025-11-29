package com.zentry.sigea.module_notificaciones.core.ports;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;

/**
 * Puerto de salida para envío de mensajes por WhatsApp
 * Define el contrato para servicios de WhatsApp externos
 */
public interface IWhatsAppService {
    
    /**
     * Envía una notificación por WhatsApp
     * @param notificacion Entidad de dominio con los datos de la notificación
     * @param telefono Número de teléfono en formato internacional (+código número)
     * @param nombreDestinatario Nombre del destinatario para personalizar el mensaje
     * @return true si el envío fue exitoso, false en caso contrario
     */
    boolean enviar(NotificacionDomainEntity notificacion, String telefono, String nombreDestinatario);
}
