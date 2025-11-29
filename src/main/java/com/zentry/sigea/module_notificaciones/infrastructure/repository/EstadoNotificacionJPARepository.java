package com.zentry.sigea.module_notificaciones.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.EstadoNotificacionEntity;

public interface EstadoNotificacionJPARepository extends JpaRepository<EstadoNotificacionEntity, UUID> {
    public Optional<EstadoNotificacionEntity> findByCodigo(String codigo);
}
