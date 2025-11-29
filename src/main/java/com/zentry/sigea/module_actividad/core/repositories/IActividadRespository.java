package com.zentry.sigea.module_actividad.core.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;

public interface IActividadRespository {
    public boolean save(ActividadDomainEntity actividadDomainEntity);
    public Optional<ActividadDomainEntity> findById(String id);
    public List<ActividadDomainEntity> findByOrganizadorId(String organizadorId);
    public List<ActividadDomainEntity> findAll();
    public List<String> findAllIds();
    public List<ActividadDomainEntity> findByEstadoActividadId(String statusId);
    public List<ActividadDomainEntity> findByTipoActividadId(String tipoActividadId);
    public List<ActividadDomainEntity> findByDateRange(LocalDate startDate , LocalDate endDate);
    public List<ActividadDomainEntity> findActiveActivities(String codigo);
    public boolean existsById(String id);
    public void deleteById(String id);
}
