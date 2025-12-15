package com.zentry.sigea.module_pago.core.repository;

import java.util.List;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.PagoRequest;

public interface IPagoRepository {
    public boolean save(PagoDomainEntity pagoDomainEntity);

    public PagoDomainEntity guardarPago(PagoRequest request);

    public List<PagoDomainEntity> findAll();

}
