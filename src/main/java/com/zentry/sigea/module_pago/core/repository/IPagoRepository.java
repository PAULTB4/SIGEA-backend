package com.zentry.sigea.module_pago.core.repository;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;

public interface IPagoRepository {
    public boolean save(PagoDomainEntity pagoDomainEntity);
    
}
