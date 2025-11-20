package com.zentry.sigea.module_pago.infrastucture.database.mappers;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.MetodoPagoEntity;

public class MetodoPagoMapper {

    /*
        * Convierte de entidad JPA a entidad de dominio
    */
    public static MetodoPagoDomainEntity toDomainEntity(MetodoPagoEntity metodoPagoEntity) {
            if (metodoPagoEntity == null) {
                return null;
            }
            MetodoPagoDomainEntity metodoPagoDomainEntity = new MetodoPagoDomainEntity();
            metodoPagoDomainEntity.setId(metodoPagoEntity.getIdMetodoPago().toString());
            metodoPagoDomainEntity.setNombre(metodoPagoEntity.getNombre());
            metodoPagoDomainEntity.setDescripcion(metodoPagoEntity.getDescripcion());
            return metodoPagoDomainEntity;
        }

    /*
        * Convierte de entidad de dominio a entidad JPA
    */

    public static MetodoPagoEntity toEntity(MetodoPagoDomainEntity metodoPagoDomainEntity) {
        if (metodoPagoDomainEntity == null) {
            return null;
        }
        MetodoPagoEntity metodoPagoEntity = new MetodoPagoEntity();
        metodoPagoEntity.setId(metodoPagoDomainEntity.getId());
        metodoPagoEntity.setNombre(metodoPagoDomainEntity.getNombre());
        metodoPagoEntity.setDescripcion(metodoPagoDomainEntity.getDescripcion());
        return metodoPagoEntity;
    }
        
    }
