package com.zentry.sigea.module_pago.services.usecase.metodopago;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IMetodoPagoRepository;

@Component
public class ListarMetodoPago {

    private final IMetodoPagoRepository metodoPagoRepository;

    public ListarMetodoPago(IMetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public List<MetodoPagoDomainEntity> execute() {
        return metodoPagoRepository.findAll();
    }

}
