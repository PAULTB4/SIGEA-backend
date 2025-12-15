package com.zentry.sigea.module_pago.services.usecase.metodopago;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IMetodoPagoRepository;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.MetodoPagoRequest;

@Component
public class CrearMetodoPagoUseCase {

    private final IMetodoPagoRepository metodoPagoRepository;

    public CrearMetodoPagoUseCase(IMetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public MetodoPagoDomainEntity execute(MetodoPagoRequest request) {
        MetodoPagoDomainEntity nuevoMetodoPago = MetodoPagoDomainEntity.create(request.getEtiqueta(),
                request.getDescripcion());

        metodoPagoRepository.save(nuevoMetodoPago);

        return nuevoMetodoPago;

    }

}
