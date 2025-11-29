package com.zentry.sigea.module_notificaciones.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.NotificacionEntity;

public interface NotificacionJPARepository extends JpaRepository<NotificacionEntity, UUID> {
    
    @Query("SELECT n FROM NotificacionEntity n WHERE n.usuario.id = :usuarioId")
    public List<NotificacionEntity> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    @Query("SELECT n FROM NotificacionEntity n WHERE n.actividad.id = :actividadId")
    public List<NotificacionEntity> findByActividadId(@Param("actividadId") UUID actividadId);
    
    @Query("SELECT n FROM NotificacionEntity n WHERE n.estadoNotificacion.id = :estadoId")
    public List<NotificacionEntity> findByEstadoNotificacionId(@Param("estadoId") UUID estadoId);
    
    @Query("SELECT n FROM NotificacionEntity n WHERE n.tipoNotificacion.id = :tipoId")
    public List<NotificacionEntity> findByTipoNotificacionId(@Param("tipoId") UUID tipoId);
    
    @Query("SELECT n FROM NotificacionEntity n WHERE n.tipoNotificacion.codigo = :codigo")
    public List<NotificacionEntity> findByTipoNotificacionCodigo(@Param("codigo") String codigo);
    
    @Query("SELECT n FROM NotificacionEntity n WHERE n.usuario.id = :usuarioId AND n.tipoNotificacion.codigo = :codigo")
    public List<NotificacionEntity> findByUsuarioIdAndTipoNotificacionCodigo(
        @Param("usuarioId") UUID usuarioId,
        @Param("codigo") String codigo
    );
    
    @Query("DELETE FROM NotificacionEntity n WHERE n.usuario.id = :usuarioId")
    public void deleteByUsuarioId(@Param("usuarioId") UUID usuarioId);
}
