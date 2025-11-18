package com.zentry.sigea.module_actividad.infrastructure.database.mappers;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.TipoActividadEntity;

/**
 * Mapper para convertir entre TipoActividad (dominio) y TipoActividadEntity (JPA)
 */

public class TipoActividadMapper {
    
    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public static TipoActividadDomainEntity toDomain(TipoActividadEntity tipoActividadEntity) {
        if (tipoActividadEntity == null) {
            return null;
        }
        
        TipoActividadDomainEntity tipoActividadDomainEntity = new TipoActividadDomainEntity();
        
        tipoActividadDomainEntity.setTipoActividadId(tipoActividadEntity.getId().toString());
        tipoActividadDomainEntity.setNombreActividad(tipoActividadEntity.getNombreActividad());
        tipoActividadDomainEntity.setDescripcion(tipoActividadEntity.getDescripcion());
        tipoActividadDomainEntity.setCreatedAt(tipoActividadEntity.getCreatedAt());
        tipoActividadDomainEntity.setUpdatedAt(tipoActividadEntity.getUpdatedAt());

        return tipoActividadDomainEntity;
    }
    
    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public static TipoActividadEntity toEntity(TipoActividadDomainEntity tipoActividadDomainEntity) {
        if (tipoActividadDomainEntity == null) {
            return null;
        }
        
        TipoActividadEntity tipoActividadEntity = new TipoActividadEntity();
        
        if (tipoActividadDomainEntity.getTipoActividadId() != null) {
            tipoActividadEntity.setId(java.util.UUID.fromString(tipoActividadDomainEntity.getTipoActividadId()));
        }
        tipoActividadEntity.setNombreActividad(tipoActividadDomainEntity.getNombreActividad());
        tipoActividadEntity.setDescripcion(tipoActividadDomainEntity.getDescripcion());
        tipoActividadEntity.setCreatedAt(tipoActividadDomainEntity.getCreatedAt());
        tipoActividadEntity.setUpdatedAt(tipoActividadDomainEntity.getUpdatedAt());
        
        return tipoActividadEntity;
    }
}