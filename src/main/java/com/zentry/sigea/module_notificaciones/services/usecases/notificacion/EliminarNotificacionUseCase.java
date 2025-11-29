package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

/**
 * Caso de uso: Eliminar una notificación
 */
@Component
public class EliminarNotificacionUseCase {

    private final INotificacionRepository notificacionRepository;

    public EliminarNotificacionUseCase(INotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public void execute(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la notificación con ID: " + id);
        }

        notificacionRepository.deleteById(id);
    }

    /**
     * Eliminar todas las notificaciones de un usuario
     */
    public void eliminarPorUsuario(String usuarioId) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }

        notificacionRepository.deleteByUsuarioId(usuarioId);
    }
}
