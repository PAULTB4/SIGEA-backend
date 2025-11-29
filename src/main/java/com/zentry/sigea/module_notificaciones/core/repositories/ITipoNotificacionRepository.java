package com.zentry.sigea.module_notificaciones.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;

/**
 * Repositorio del dominio para tipos de notificación
 */
public interface ITipoNotificacionRepository {
    TipoNotificacionDomainEntity save(TipoNotificacionDomainEntity tipo);
    Optional<TipoNotificacionDomainEntity> findById(String id);
    Optional<TipoNotificacionDomainEntity> findByCodigo(String codigo);
    List<TipoNotificacionDomainEntity> findAll();
    boolean existsById(String id);
    boolean existsByCodigo(String codigo);
    void deleteById(String id);
}
