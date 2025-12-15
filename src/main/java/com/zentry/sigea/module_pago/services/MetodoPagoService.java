package com.zentry.sigea.module_pago.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.MetodoPagoRequest;
import com.zentry.sigea.module_pago.services.usecase.metodopago.CrearMetodoPagoUseCase;
import com.zentry.sigea.module_pago.services.usecase.metodopago.ListarMetodoPago;

@Service
public class MetodoPagoService {
    private final CrearMetodoPagoUseCase crearMetodoPagoUseCase;
    private final ListarMetodoPago listarMetodoPagoUseCase;

    public MetodoPagoService(CrearMetodoPagoUseCase crearMetodoPagoUseCase, ListarMetodoPago listarMetodoPagoUseCase) {
        this.crearMetodoPagoUseCase = crearMetodoPagoUseCase;
        this.listarMetodoPagoUseCase = listarMetodoPagoUseCase;
    }

    public void crearMetodoPago(MetodoPagoRequest metodoPagoRequest) {
        crearMetodoPagoUseCase.execute(metodoPagoRequest);
    }

    public List<MetodoPagoDomainEntity> listarMetodosPago() {
        return listarMetodoPagoUseCase.execute();
    }

}
