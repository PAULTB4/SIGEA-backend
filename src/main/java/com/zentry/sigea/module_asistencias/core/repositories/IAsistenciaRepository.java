package com.zentry.sigea.module_asistencias.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;

public interface IAsistenciaRepository {
    void save(AsistenciaDomainEntity asistenciaDomainEntity);
    Optional<AsistenciaDomainEntity> findById(String id);
    List<AsistenciaDomainEntity> findByInscripcionId(String inscripcionId);
    List<AsistenciaDomainEntity> findBySesionId(String sesionId);
    List<AsistenciaDomainEntity> findBySesionIdAndPresente(String sesionId, Boolean presente);
    boolean existsById(String id);
}
    