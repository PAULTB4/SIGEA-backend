package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

/**
 * Caso de uso: Listar todas las notificaciones
 */
@Component
public class ListarNotificacionesUseCase {

    private final INotificacionRepository notificacionRepository;

    public ListarNotificacionesUseCase(INotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<NotificacionDomainEntity> execute() {
        return notificacionRepository.findAll();
    }

    public List<NotificacionDomainEntity> executeByUsuarioId(String usuarioId) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public List<NotificacionDomainEntity> executeByActividadId(String actividadId) {
        if (actividadId == null || actividadId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la actividad es obligatorio");
        }
        return notificacionRepository.findByActividadId(actividadId);
    }

    public List<NotificacionDomainEntity> executeByTipoEvento(String tipoEvento) {
        if (tipoEvento == null || tipoEvento.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de evento es obligatorio");
        }
        return notificacionRepository.findByTipoEvento(tipoEvento);
    }

    public List<NotificacionDomainEntity> executeByUsuarioIdAndTipoEvento(String usuarioId, String tipoEvento) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        if (tipoEvento == null || tipoEvento.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de evento es obligatorio");
        }
        return notificacionRepository.findByUsuarioIdAndTipoEvento(usuarioId, tipoEvento);
    }
}
