package com.zentry.sigea.module_notificaciones.infrastructure.database.mappers;

import com.zentry.sigea.module_notificaciones.core.entities.CanalNotificacion;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.NotificacionEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

/**
 * Mapper para convertir entre Notificacion (dominio) y NotificacionEntity (JPA)
 */
public class NotificacionMapper {

    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public static NotificacionEntity toEntity(
        NotificacionDomainEntity notificacionDomainEntity,
        UsuarioEntity usuarioEntity,
        ActividadEntity actividadEntity
    ) {
        if (notificacionDomainEntity == null) {
            return null;
        }

        NotificacionEntity notificacionEntity = new NotificacionEntity();

        notificacionEntity.setUsuario(usuarioEntity);
        notificacionEntity.setActividad(actividadEntity);
        notificacionEntity.setTipoNotificacion(
            TipoNotificacionMapper.toEntity(
                notificacionDomainEntity.getTipoNotificacion()
            )
        );
        notificacionEntity.setMensaje(notificacionDomainEntity.getMensaje());
        notificacionEntity.setFechaEnvio(notificacionDomainEntity.getFechaEnvio());
        notificacionEntity.setEstadoNotificacion(
            EstadoNotificacionMapper.toEntity(
                notificacionDomainEntity.getEstadoNotificacion()
            )
        );
        notificacionEntity.setCanal(
            notificacionDomainEntity.getCanal() != null ? 
            notificacionDomainEntity.getCanal().name() : null
        );
        notificacionEntity.setCreatedAt(notificacionDomainEntity.getCreatedAt());
        notificacionEntity.setUpdatedAt(notificacionDomainEntity.getUpdatedAt());

        return notificacionEntity;
    }

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public static NotificacionDomainEntity toDomain(NotificacionEntity notificacionEntity) {
        if (notificacionEntity == null) {
            return null;
        }

        NotificacionDomainEntity notificacionDomainEntity = new NotificacionDomainEntity();

        notificacionDomainEntity.setId(notificacionEntity.getId().toString());
        notificacionDomainEntity.setUsuarioId(
            notificacionEntity.getUsuario() != null ? 
            notificacionEntity.getUsuario().getId().toString() : null
        );
        notificacionDomainEntity.setActividadId(
            notificacionEntity.getActividad() != null ? 
            notificacionEntity.getActividad().getId().toString() : null
        );
        notificacionDomainEntity.setTipoNotificacion(
            TipoNotificacionMapper.toDomain(
                notificacionEntity.getTipoNotificacion()
            )
        );
        notificacionDomainEntity.setMensaje(notificacionEntity.getMensaje());
        notificacionDomainEntity.setFechaEnvio(notificacionEntity.getFechaEnvio());
        notificacionDomainEntity.setEstadoNotificacion(
            EstadoNotificacionMapper.toDomain(
                notificacionEntity.getEstadoNotificacion()
            )
        );
        notificacionDomainEntity.setCanal(
            notificacionEntity.getCanal() != null ? 
            CanalNotificacion.fromString(notificacionEntity.getCanal()) : null
        );
        notificacionDomainEntity.setCreatedAt(notificacionEntity.getCreatedAt());
        notificacionDomainEntity.setUpdatedAt(notificacionEntity.getUpdatedAt());

        return notificacionDomainEntity;
    }
}
