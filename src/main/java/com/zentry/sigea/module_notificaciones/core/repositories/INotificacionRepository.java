package com.zentry.sigea.module_notificaciones.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;

/**
 * Repositorio del dominio para la gestión de notificaciones
 * Port (Hexagonal Architecture): Define el contrato para persistencia
 */
public interface INotificacionRepository {
    // Guardar y actualizar
    boolean save(NotificacionDomainEntity notificacion);
    
    // Consultas básicas
    Optional<NotificacionDomainEntity> findById(String id);
    List<NotificacionDomainEntity> findAll();
    boolean existsById(String id);
    
    // Consultas por usuario y estado
    List<NotificacionDomainEntity> findByUsuarioId(String usuarioId);
    List<NotificacionDomainEntity> findNoLeidasByUsuarioId(String usuarioId);
    
    // Consultas por relaciones
    List<NotificacionDomainEntity> findByActividadId(String actividadId);
    List<NotificacionDomainEntity> findByEstadoNotificacionId(String estadoId);
    List<NotificacionDomainEntity> findByTipoNotificacionId(String tipoId);
    
    // Consultas por tipo de evento
    List<NotificacionDomainEntity> findByTipoEvento(String tipoEvento);
    List<NotificacionDomainEntity> findByUsuarioIdAndTipoEvento(String usuarioId, String tipoEvento);
    
    // Eliminar
    void deleteById(String id);
    void deleteByUsuarioId(String usuarioId);
}
