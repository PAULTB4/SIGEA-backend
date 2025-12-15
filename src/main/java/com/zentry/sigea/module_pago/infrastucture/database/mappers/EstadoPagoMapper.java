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
        if (estadoPagoEntity.getIdEstadoPago() != null) {
            estadoPagoDomainEntity.setIdEstadoPago(estadoPagoEntity.getIdEstadoPago().toString());
        }
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
        if (estadoPagoDomainEntity.getIdEstadoPago() != null) {
            estadoPagoEntity.setIdEstadoPago(java.util.UUID.fromString(estadoPagoDomainEntity.getIdEstadoPago()));
        }
        estadoPagoEntity.setDescripcion(estadoPagoDomainEntity.getDescripcion());
        estadoPagoEntity.setEtiqueta(estadoPagoDomainEntity.getEtiqueta());
        return estadoPagoEntity;
    }
}
