package com.zentry.sigea.module_informe.core.repositories;

import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import java.util.List;
import java.util.Optional;

public interface ITipoInformeRepository {
    Optional<TipoInformeDomainEntity> findById(String id);
    Optional<TipoInformeDomainEntity> findByCodigo(String codigo);
    List<TipoInformeDomainEntity> findAll();
    TipoInformeDomainEntity save(TipoInformeDomainEntity tipoInformeDomainEntity);
    void deleteById(String id);
}
