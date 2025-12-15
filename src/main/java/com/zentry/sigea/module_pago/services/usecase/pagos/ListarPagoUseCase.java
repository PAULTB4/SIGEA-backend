package com.zentry.sigea.module_pago.services.usecase.pagos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IPagoRepository;

@Component
public class ListarPagoUseCase {
    private final IPagoRepository pagoRepository;

    public ListarPagoUseCase(IPagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<PagoDomainEntity> execute() {
        return pagoRepository.findAll();
    }

}
