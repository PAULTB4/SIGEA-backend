package com.zentry.sigea.module_pago.infrastucture.database.mappers;

import java.util.Optional;
import java.util.UUID;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.EstadoPagoEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.MetodoPagoEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.PagoEntity;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.PagoRequest;

public class PagoMapper {

    /*
     * Convierte de dominio → entidad JPA
     */
    public static PagoEntity toEntity(PagoDomainEntity domain,
            InscripcionEntity inscripcion,
            MetodoPagoEntity metodoPago,
            EstadoPagoEntity estadoPago) {

        if (domain == null)
            return null;

        PagoEntity entity = new PagoEntity();

        // Solo setear ID si estás ACTUALIZANDO (si existe)
        if (domain.getIdPago() != null) {
            entity.setIdPago(UUID.fromString(domain.getIdPago()));
        }

        entity.setMonto(domain.getMonto());
        entity.setFechaPago(domain.getFechaPago());
        entity.setReferenciaExterna(domain.getReferenciaExt());
        entity.setUrlMercadoPago(domain.getUrlMercadoPago());

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

        if (entity == null)
            return null;

        PagoDomainEntity domain = new PagoDomainEntity();

        domain.setIdPago(entity.getIdPago().toString());
        domain.setMonto(entity.getMonto());
        domain.setMoneda(entity.getMoneda());
        domain.setFechaPago(entity.getFechaPago());
        domain.setReferenciaExt(entity.getReferenciaExterna());
        domain.setUrlMercadoPago(entity.getUrlMercadoPago());

        domain.setMetodoId(entity.getMetodoPago().getIdMetodoPago().toString());
        domain.setEstadoId(entity.getEstadoPago().getIdEstadoPago().toString());

        domain.setInscripcionId(entity.getInscripcion().getId().toString());
        domain.setUsuarioId(entity.getInscripcion().getUsuario().getId().toString()); // Map usuarioId
        domain.setActividadId(entity.getInscripcion().getActividad().getId().toString()); // Map actividadId
        domain.setUsuarioDni(entity.getInscripcion().getUsuario().getDni()); // Map usuarioDni

        return domain;
    }

    public static PagoDomainEntity toDomain(PagoRequest request,
            Optional<MetodoPagoDomainEntity> metodoPagoOpt,
            Optional<EstadoPagoDomainEntity> estadoPagoOpt) {

        if (request == null)
            return null;

        MetodoPagoDomainEntity metodo = metodoPagoOpt
                .orElseThrow(() -> new IllegalArgumentException("Metodo de pago no valido"));
        EstadoPagoDomainEntity estado = estadoPagoOpt
                .orElseThrow(() -> new IllegalArgumentException("Estado de pago no valido"));

        PagoDomainEntity domain = new PagoDomainEntity();

        if (request.inscripcionId() != null) {
            domain.setInscripcionId(request.inscripcionId().toString());
        }

        if (request.monto() != null) {
            domain.setMonto(request.monto());
        }

        domain.setMoneda(request.moneda());
        domain.setReferenciaExt(request.referenciaExterna());

        // Relaciones
        domain.setMetodoId(metodo.getIdMetodoPago());
        domain.setEstadoId(estado.getIdEstadoPago());

        return domain;
    }
}