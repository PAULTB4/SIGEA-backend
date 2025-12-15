package com.zentry.sigea.module_pago.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.EstadoPagoRequest;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.MetodoPagoRequest;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.PagoRequest;

public interface IPago {

    Object pagarConYape(BigDecimal monto, String descripcion);

    Object consultarPago(Object request);

    Map<String, Object> createMetadata(Map<String, Object> request);

    Object guardarPago(PagoRequest request);

    List<PagoDomainEntity> findAll();
}
