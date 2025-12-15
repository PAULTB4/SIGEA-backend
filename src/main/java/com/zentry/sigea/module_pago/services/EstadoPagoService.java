package com.zentry.sigea.module_pago.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.EstadoPagoRequest;
import com.zentry.sigea.module_pago.services.usecase.estadopago.CrearEstadoPagoUseCase;
import com.zentry.sigea.module_pago.services.usecase.estadopago.ListarEstadoPagoUseCase;

@Service
public class EstadoPagoService {
    private final CrearEstadoPagoUseCase crearEstadoPagoUseCase;
    private final ListarEstadoPagoUseCase listarEstadoPagoUseCase;

    public EstadoPagoService(CrearEstadoPagoUseCase crearEstadoPagoUseCase,
            ListarEstadoPagoUseCase listarEstadoPagoUseCase) {
        this.crearEstadoPagoUseCase = crearEstadoPagoUseCase;
        this.listarEstadoPagoUseCase = listarEstadoPagoUseCase;
    }

    public void crearEstadoPago(EstadoPagoRequest estadoPagoRequest) {
        crearEstadoPagoUseCase.execute(estadoPagoRequest);
    }

    public List<EstadoPagoDomainEntity> listarEstadosPago() {
        return listarEstadoPagoUseCase.execute();
    }

}
