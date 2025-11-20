package com.zentry.sigea.module_pago.infrastucture.database.mappers;

import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.EstadoPagoEntity;

public class EstadoPagoMapper {

    /*
        * Convertir de entidad JPA a entidad de dominio
    */
    
    public static EstadoPagoDomainEntity toDomainEntity(EstadoPagoEntity estadoPagoEntity) {
        if (estadoPagoEntity == null) {
            return null;
        }
        EstadoPagoDomainEntity estadoPagoDomainEntity = new EstadoPagoDomainEntity();
        estadoPagoDomainEntity.setIdEstadoPago(estadoPagoEntity.getIdEstadoPago());
        estadoPagoDomainEntity.setDescripcion(estadoPagoEntity.getDescripcion());
        estadoPagoDomainEntity.setEtiqueta(estadoPagoEntity.getEtiqueta());
        return estadoPagoDomainEntity;
    }

    /*
        * Convertir de entidad de dominio a entidad JPA
    */
    public static EstadoPagoEntity toEntity(EstadoPagoDomainEntity estadoPagoDomainEntity) {
        if (estadoPagoDomainEntity == null) {
            return null;
        }
        EstadoPagoEntity estadoPagoEntity = new EstadoPagoEntity();
        estadoPagoEntity.setIdEstadoPago(estadoPagoDomainEntity.getIdEstadoPago());
        estadoPagoEntity.setDescripcion(estadoPagoDomainEntity.getDescripcion());
        estadoPagoEntity.setEtiqueta(estadoPagoDomainEntity.getEtiqueta());
        return estadoPagoEntity;
    }
}
