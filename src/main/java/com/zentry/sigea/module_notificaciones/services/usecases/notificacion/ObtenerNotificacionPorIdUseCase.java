package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

/**
 * Caso de uso: Obtener una notificación por ID
 */
@Component
public class ObtenerNotificacionPorIdUseCase {

    private final INotificacionRepository notificacionRepository;

    public ObtenerNotificacionPorIdUseCase(INotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public NotificacionDomainEntity execute(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }

        return notificacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró la notificación con ID: " + id
            ));
    }
}
