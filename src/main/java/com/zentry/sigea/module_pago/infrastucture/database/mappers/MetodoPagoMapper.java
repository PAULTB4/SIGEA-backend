package com.zentry.sigea.module_pago.infrastucture.database.mappers;

import java.util.UUID;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.MetodoPagoEntity;

public class MetodoPagoMapper {

    /*
     * Convierte de entidad JPA a entidad de dominio
     */
    public static MetodoPagoDomainEntity toDomainEntity(MetodoPagoEntity entity) {
        if (entity == null) {
            return null;
        }
        MetodoPagoDomainEntity domain = new MetodoPagoDomainEntity();
        if (entity.getIdMetodoPago() != null) {
            domain.setIdMetodoPago(entity.getIdMetodoPago().toString());
        }
        domain.setDescripcion(entity.getDescripcion());
        domain.setEtiqueta(entity.getEtiqueta());
        return domain;
    }

    /*
     * Convierte de entidad de dominio a entidad JPA
     */
    public static MetodoPagoEntity toEntity(MetodoPagoDomainEntity domain) {
        if (domain == null) {
            return null;
        }
        MetodoPagoEntity entity = new MetodoPagoEntity();
        if (domain.getIdMetodoPago() != null && !domain.getIdMetodoPago().isEmpty()) {
            entity.setIdMetodoPago(UUID.fromString(domain.getIdMetodoPago()));
        }
        entity.setDescripcion(domain.getDescripcion());
        entity.setEtiqueta(domain.getEtiqueta());
        return entity;
    }
}
