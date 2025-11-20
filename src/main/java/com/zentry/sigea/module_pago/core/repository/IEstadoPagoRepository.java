package com.zentry.sigea.module_pago.core.repository;

import java.util.Optional;

import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;

public interface IEstadoPagoRepository {
    public Optional<EstadoPagoDomainEntity> findById(String id);
    public boolean save(EstadoPagoDomainEntity estadoPagoDomainEntity);
    public java.util.List<EstadoPagoDomainEntity> findAll();
    public void deleteById(String id);
    
}
