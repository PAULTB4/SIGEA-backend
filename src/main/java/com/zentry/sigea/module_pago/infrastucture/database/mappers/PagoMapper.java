package com.zentry.sigea.module_pago.infrastucture.database.mappers;

import java.util.UUID;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.EstadoPagoEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.MetodoPagoEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.PagoEntity;

public class PagoMapper {

    /*
     * Convierte de dominio → entidad JPA
     */
    public static PagoEntity toEntity(PagoDomainEntity domain, 
                                      InscripcionEntity inscripcion,
                                      MetodoPagoEntity metodoPago,
                                      EstadoPagoEntity estadoPago) {

        if (domain == null) return null;

        PagoEntity entity = new PagoEntity();

        // Solo setear ID si estás ACTUALIZANDO (si existe)
        if (domain.getIdPago() != null) {
            entity.setIdPago(UUID.fromString(domain.getIdPago()));
        }

        

        entity.setMonto(domain.getMonto());
        entity.setFechaPago(domain.getFechaPago());
        entity.setReferenciaExt(domain.getReferenciaExterna());
        entity.setUltimos4(domain.getUltimos4());
        entity.setBin(domain.getBin());
        entity.setRawRespuesta(domain.getRawRespuesta());

        // Relaciones correctas
        entity.setInscripcion(inscripcion);
        entity.setMetodoPago(metodoPago);
        entity.setEstadoPago(estadoPago);

        return entity;
    }

    /*
     * Convierte de JPA → dominio
     */
    public static PagoDomainEntity toDomain(PagoEntity entity) {

        if (entity == null) return null;

        PagoDomainEntity domain = new PagoDomainEntity();

        domain.setIdPago(entity.getIdPago().toString());
        domain.setMonto(entity.getMonto());
        domain.setFechaPago(entity.getFechaPago());
        domain.setReferenciaExterna(entity.getReferenciaExt());
        domain.setUltimos4(entity.getUltimos4());
        domain.setBin(entity.getBin());
        domain.setRawRespuesta(entity.getRawRespuesta());

        domain.setIdMetodoPago(entity.getMetodoPago().getIdMetodoPago());
        domain.setIdEstadoPago(entity.getEstadoPago().getIdEstadoPago());

        domain.setInscripcionId(entity.getInscripcion().getId());

        return domain;
    }
}