package com.zentry.sigea.module_notificaciones.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;

/**
 * Repositorio del dominio para estados de notificación
 */
public interface IEstadoNotificacionRepository {
    EstadoNotificacionDomainEntity save(EstadoNotificacionDomainEntity estado);
    Optional<EstadoNotificacionDomainEntity> findById(String id);
    Optional<EstadoNotificacionDomainEntity> findByCodigo(String codigo);
    List<EstadoNotificacionDomainEntity> findAll();
    boolean existsById(String id);
    boolean existsByCodigo(String codigo);
    void deleteById(String id);
}
