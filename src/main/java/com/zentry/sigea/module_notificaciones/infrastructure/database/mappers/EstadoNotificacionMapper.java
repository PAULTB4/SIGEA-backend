package com.zentry.sigea.module_notificaciones.infrastructure.database.mappers;

import java.util.UUID;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.EstadoNotificacionEntity;

/**
 * Mapper para convertir entre EstadoNotificacion (dominio) y EstadoNotificacionEntity (JPA)
 */
public class EstadoNotificacionMapper {
    
    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public static EstadoNotificacionEntity toEntity(EstadoNotificacionDomainEntity estadoNotificacionDomainEntity) {
        if (estadoNotificacionDomainEntity == null) {
            return null;
        }
        
        EstadoNotificacionEntity estadoNotificacionEntity = new EstadoNotificacionEntity();

        // IMPORTANTE: Incluir el ID para que Hibernate sepa que es una entidad existente
        if (estadoNotificacionDomainEntity.getId() != null) {
            estadoNotificacionEntity.setId(UUID.fromString(estadoNotificacionDomainEntity.getId()));
        }
        
        estadoNotificacionEntity.setCodigo(estadoNotificacionDomainEntity.getCodigo());
        estadoNotificacionEntity.setEtiqueta(estadoNotificacionDomainEntity.getEtiqueta());
        
        return estadoNotificacionEntity;
    }

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public static EstadoNotificacionDomainEntity toDomain(EstadoNotificacionEntity estadoNotificacionEntity) {
        if (estadoNotificacionEntity == null) {
            return null;
        }

        EstadoNotificacionDomainEntity estadoNotificacionDomainEntity = new EstadoNotificacionDomainEntity();

        estadoNotificacionDomainEntity.setId(estadoNotificacionEntity.getId().toString());
        estadoNotificacionDomainEntity.setCodigo(estadoNotificacionEntity.getCodigo());
        estadoNotificacionDomainEntity.setEtiqueta(estadoNotificacionEntity.getEtiqueta());
        
        return estadoNotificacionDomainEntity;
    }
}
