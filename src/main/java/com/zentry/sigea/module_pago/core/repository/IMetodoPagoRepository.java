package com.zentry.sigea.module_pago.core.repository;

import java.util.Optional;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;

public interface  IMetodoPagoRepository {
    public Optional<MetodoPagoDomainEntity> findById(String id);
    public boolean save(MetodoPagoDomainEntity metodoPagoDomainEntity);
    public java.util.List<MetodoPagoDomainEntity> findAll();
    public void deleteById(String id);
    
}
