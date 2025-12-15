package com.zentry.sigea.module_pago.services.usecase.estadopago;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IEstadoPagoRepository;

@Component
public class ListarEstadoPagoUseCase {

    private final IEstadoPagoRepository estadoPagoRepository;

    public ListarEstadoPagoUseCase(IEstadoPagoRepository estadoPagoRepository) {
        this.estadoPagoRepository = estadoPagoRepository;
    }

    public List<EstadoPagoDomainEntity> execute() {
        return estadoPagoRepository.findAll();
    }

}
