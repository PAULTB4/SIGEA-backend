package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

/**
 * Caso de uso: Actualizar el estado de una notificación existente
 * Usado principalmente cuando falla el envío por un canal externo
 */
@Component
public class ActualizarEstadoNotificacionUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ActualizarEstadoNotificacionUseCase.class);
    
    private final INotificacionRepository notificacionRepository;
    private final IEstadoNotificacionRepository estadoNotificacionRepository;
    
    public ActualizarEstadoNotificacionUseCase(
        INotificacionRepository notificacionRepository,
        IEstadoNotificacionRepository estadoNotificacionRepository
    ) {
        this.notificacionRepository = notificacionRepository;
        this.estadoNotificacionRepository = estadoNotificacionRepository;
    }
    
    /**
     * Actualiza el estado de una notificación
     * @param notificacionId ID de la notificación
     * @param codigoEstado Código del nuevo estado (ej: FALLIDA, ENVIADA)
     * @return true si se actualizó correctamente
     */
    public boolean execute(String notificacionId, String codigoEstado) {
        try {
            // Buscar la notificación
            NotificacionDomainEntity notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró la notificación con ID: " + notificacionId
                ));
            
            // Buscar el estado por código
            EstadoNotificacionDomainEntity nuevoEstado = 
                estadoNotificacionRepository.findByCodigo(codigoEstado)
                    .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el estado con código: " + codigoEstado
                    ));
            
            // Cambiar el estado usando el método del dominio
            notificacion.cambiarEstado(nuevoEstado);
            
            // Guardar los cambios
            boolean actualizado = notificacionRepository.save(notificacion);
            
            if (actualizado) {
                logger.info("Estado de notificación {} actualizado a {}", 
                    notificacionId, codigoEstado);
            } else {
                logger.error("Error al actualizar estado de notificación {}", notificacionId);
            }
            
            return actualizado;
            
        } catch (Exception e) {
            logger.error("Error al actualizar estado de notificación {}: {}", 
                notificacionId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Marcar una notificación como leída
     */
    public NotificacionDomainEntity marcarComoLeida(String notificacionId) {
        NotificacionDomainEntity notificacion = notificacionRepository.findById(notificacionId)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró la notificación con ID: " + notificacionId
            ));
        
        EstadoNotificacionDomainEntity estadoLeida = 
            estadoNotificacionRepository.findByCodigo("LEIDA")
                .orElseThrow(() -> new IllegalStateException("Estado LEIDA no encontrado"));
        
        notificacion.cambiarEstado(estadoLeida);
        notificacionRepository.save(notificacion);
        
        logger.info("Notificación {} marcada como leída", notificacionId);
        return notificacion;
    }

    /**
     * Marcar todas las notificaciones de un usuario como leídas
     */
    public void marcarTodasComoLeidas(String usuarioId) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        
        EstadoNotificacionDomainEntity estadoLeida = 
            estadoNotificacionRepository.findByCodigo("LEIDA")
                .orElseThrow(() -> new IllegalStateException("Estado LEIDA no encontrado"));
        
        notificacionRepository.findByUsuarioId(usuarioId).forEach(notificacion -> {
            notificacion.cambiarEstado(estadoLeida);
            notificacionRepository.save(notificacion);
        });
        
        logger.info("Todas las notificaciones del usuario {} marcadas como leídas", usuarioId);
    }
}
