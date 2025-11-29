package com.zentry.sigea.module_informe.core.repositories;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInformeRepository {
    InformeDomainEntity save(InformeDomainEntity informe);

    Optional<InformeDomainEntity> findById(UUID id);

    default Optional<InformeDomainEntity> findById(String idInforme) {
        UUID informeId = UUID.fromString(idInforme);
        return findById(informeId);
    }

    List<InformeDomainEntity> findAll();

    void deleteById(UUID id);

    default void deleteById(String idInforme) {
        UUID informeId = UUID.fromString(idInforme);
        deleteById(informeId);
    }

    boolean existsById(UUID id);

    default boolean existsById(String idInforme) {
        UUID informeId = UUID.fromString(idInforme);
        return existsById(informeId);
    }
}
