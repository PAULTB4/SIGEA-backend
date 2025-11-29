package com.zentry.sigea.module_notificaciones.infrastructure.database.mappers;

import java.util.UUID;

import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.TipoNotificacionEntity;

/**
 * Mapper para convertir entre TipoNotificacion (dominio) y TipoNotificacionEntity (JPA)
 */
public class TipoNotificacionMapper {
    
    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public static TipoNotificacionEntity toEntity(TipoNotificacionDomainEntity tipoNotificacionDomainEntity) {
        if (tipoNotificacionDomainEntity == null) {
            return null;
        }
        
        TipoNotificacionEntity tipoNotificacionEntity = new TipoNotificacionEntity();

        // IMPORTANTE: Incluir el ID para que Hibernate sepa que es una entidad existente
        if (tipoNotificacionDomainEntity.getId() != null) {
            tipoNotificacionEntity.setId(UUID.fromString(tipoNotificacionDomainEntity.getId()));
        }
        
        tipoNotificacionEntity.setCodigo(tipoNotificacionDomainEntity.getCodigo());
        tipoNotificacionEntity.setEtiqueta(tipoNotificacionDomainEntity.getEtiqueta());
        
        return tipoNotificacionEntity;
    }

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public static TipoNotificacionDomainEntity toDomain(TipoNotificacionEntity tipoNotificacionEntity) {
        if (tipoNotificacionEntity == null) {
            return null;
        }

        TipoNotificacionDomainEntity tipoNotificacionDomainEntity = new TipoNotificacionDomainEntity();

        tipoNotificacionDomainEntity.setId(tipoNotificacionEntity.getId().toString());
        tipoNotificacionDomainEntity.setCodigo(tipoNotificacionEntity.getCodigo());
        tipoNotificacionDomainEntity.setEtiqueta(tipoNotificacionEntity.getEtiqueta());
        
        return tipoNotificacionDomainEntity;
    }
}
