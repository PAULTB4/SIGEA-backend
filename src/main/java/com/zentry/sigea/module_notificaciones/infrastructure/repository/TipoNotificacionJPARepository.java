package com.zentry.sigea.module_notificaciones.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.TipoNotificacionEntity;

public interface TipoNotificacionJPARepository extends JpaRepository<TipoNotificacionEntity, UUID> {
    public Optional<TipoNotificacionEntity> findByCodigo(String codigo);
}
