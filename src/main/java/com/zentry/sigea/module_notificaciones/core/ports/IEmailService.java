package com.zentry.sigea.module_notificaciones.core.ports;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;

/**
 * Puerto de salida para envío de emails
 * Define el contrato para servicios de email externos
 */
public interface IEmailService {
    
    /**
     * Envía una notificación por correo electrónico
     * @param notificacion Entidad de dominio con los datos de la notificación
     * @param destinatario Email del destinatario
     * @param nombreDestinatario Nombre del destinatario para personalizar el email
     * @return true si el envío fue exitoso, false en caso contrario
     */
    boolean enviar(NotificacionDomainEntity notificacion, String destinatario, String nombreDestinatario);
}
