package com.zentry.sigea.module_notificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;
import com.zentry.sigea.module_notificaciones.infrastructure.database.mappers.NotificacionMapper;
import com.zentry.sigea.module_notificaciones.infrastructure.repository.NotificacionJPARepository;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@Repository
public class NotificacionRepositoryAdapter implements INotificacionRepository {
    
    private final NotificacionJPARepository notificacionJPARepository;
    private final UsuarioJPARepository usuarioJPARepository;
    private final ActividadJPARepository actividadJPARepository;

    public NotificacionRepositoryAdapter(
        NotificacionJPARepository notificacionJPARepository,
        UsuarioJPARepository usuarioJPARepository,
        ActividadJPARepository actividadJPARepository
    ) {
        this.notificacionJPARepository = notificacionJPARepository;
        this.usuarioJPARepository = usuarioJPARepository;
        this.actividadJPARepository = actividadJPARepository;
    }

    public boolean save(NotificacionDomainEntity notificacionDomainEntity) {
        try {
            UsuarioEntity usuarioEntity = usuarioJPARepository.findById(
                UUID.fromString(notificacionDomainEntity.getUsuarioId())
            ).orElse(null);

            // La actividad puede ser opcional
            ActividadEntity actividadEntity = null;
            if (notificacionDomainEntity.getActividadId() != null) {
                actividadEntity = actividadJPARepository.findById(
                    UUID.fromString(notificacionDomainEntity.getActividadId())
                ).orElse(null);
            }

            if (usuarioEntity == null) {
                return false;
            }

            notificacionJPARepository.save(
                NotificacionMapper.toEntity(
                    notificacionDomainEntity,
                    usuarioEntity,
                    actividadEntity
                )
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<NotificacionDomainEntity> findById(String id) {
        return notificacionJPARepository.findById(
            UUID.fromString(id)
        ).map(NotificacionMapper::toDomain);
    }

    public List<NotificacionDomainEntity> findByUsuarioId(String usuarioId) {
        return notificacionJPARepository.findByUsuarioId(
            UUID.fromString(usuarioId)
        ).stream()
        .map(NotificacionMapper::toDomain)
        .collect(Collectors.toList());
    }

    @Override
    public List<NotificacionDomainEntity> findNoLeidasByUsuarioId(String usuarioId) {
        return findByUsuarioId(usuarioId).stream()
            .filter(notif -> notif.estaPendiente() || !notif.estaLeida())
            .collect(Collectors.toList());
    }

    public List<NotificacionDomainEntity> findByActividadId(String actividadId) {
        return notificacionJPARepository.findByActividadId(
            UUID.fromString(actividadId)
        ).stream()
        .map(NotificacionMapper::toDomain)
        .collect(Collectors.toList());
    }

    public List<NotificacionDomainEntity> findAll() {
        return notificacionJPARepository.findAll().stream()
            .map(NotificacionMapper::toDomain)
            .collect(Collectors.toList());
    }

    public List<NotificacionDomainEntity> findByEstadoNotificacionId(String estadoId) {
        return notificacionJPARepository.findByEstadoNotificacionId(
            UUID.fromString(estadoId)
        ).stream()
        .map(NotificacionMapper::toDomain)
        .collect(Collectors.toList());
    }

    public List<NotificacionDomainEntity> findByTipoNotificacionId(String tipoId) {
        return notificacionJPARepository.findByTipoNotificacionId(
            UUID.fromString(tipoId)
        ).stream()
        .map(NotificacionMapper::toDomain)
        .collect(Collectors.toList());
    }

    public boolean existsById(String id) {
        return notificacionJPARepository.existsById(UUID.fromString(id));
    }

    public void deleteById(String id) {
        notificacionJPARepository.deleteById(UUID.fromString(id));
    }

    @Override
    public List<NotificacionDomainEntity> findByTipoEvento(String tipoEvento) {
        return notificacionJPARepository.findByTipoNotificacionCodigo(tipoEvento)
            .stream()
            .map(NotificacionMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificacionDomainEntity> findByUsuarioIdAndTipoEvento(String usuarioId, String tipoEvento) {
        return notificacionJPARepository.findByUsuarioIdAndTipoNotificacionCodigo(
            UUID.fromString(usuarioId),
            tipoEvento
        ).stream()
        .map(NotificacionMapper::toDomain)
        .collect(Collectors.toList());
    }

    @Override
    public void deleteByUsuarioId(String usuarioId) {
        notificacionJPARepository.deleteByUsuarioId(UUID.fromString(usuarioId));
    }
}
