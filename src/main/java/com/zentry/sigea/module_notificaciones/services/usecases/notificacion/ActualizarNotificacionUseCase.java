package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.ActualizarNotificacionRequest;

/**
 * Caso de uso: Actualizar una notificación existente
 */
@Component
public class ActualizarNotificacionUseCase {

    private final INotificacionRepository notificacionRepository;
    private final IEstadoNotificacionRepository estadoNotificacionRepository;

    public ActualizarNotificacionUseCase(
        INotificacionRepository notificacionRepository,
        IEstadoNotificacionRepository estadoNotificacionRepository
    ) {
        this.notificacionRepository = notificacionRepository;
        this.estadoNotificacionRepository = estadoNotificacionRepository;
    }

    public NotificacionDomainEntity execute(String id, ActualizarNotificacionRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }

        // Buscar la notificación existente
        NotificacionDomainEntity notificacion = notificacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró la notificación con ID: " + id
            ));

        // Actualizar mensaje si se proporciona
        if (request.getMensaje() != null && !request.getMensaje().trim().isEmpty()) {
            notificacion.setMensaje(request.getMensaje().trim());
        }

        // Actualizar estado si se proporciona
        if (request.getEstadoNotificacionId() != null && !request.getEstadoNotificacionId().trim().isEmpty()) {
            EstadoNotificacionDomainEntity nuevoEstado = 
                estadoNotificacionRepository.findById(request.getEstadoNotificacionId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el estado con ID: " + request.getEstadoNotificacionId()
                    ));
            notificacion.cambiarEstado(nuevoEstado);
        }

        // Guardar cambios
        notificacionRepository.save(notificacion);
        return notificacion;
    }
}
