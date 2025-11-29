package com.zentry.sigea.module_inscripciones.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;

public interface InscripcionJPARepository extends JpaRepository<InscripcionEntity, UUID> {
    
    @Query("SELECT i FROM InscripcionEntity i WHERE i.usuario.id = :usuarioId")
    public List<InscripcionEntity> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    @Query("SELECT i FROM InscripcionEntity i WHERE i.actividad.id = :actividadId")
    public List<InscripcionEntity> findByActividadId(@Param("actividadId") UUID actividadId);
    
    @Query(
        """
            SELECT i.id FROM InscripcionEntity i
            WHERE i.actividad.id IN :listActividadIds
        """
    )
    public List<UUID> findIdByListActividadIds(
        @Param("listActividadIds") List<UUID> listActividadIds
    );

    @Query("SELECT i FROM InscripcionEntity i WHERE i.usuario.id = :usuarioId AND i.actividad.id = :actividadId")
    public Optional<InscripcionEntity> findByUsuarioIdAndActividadId(
        @Param("usuarioId") UUID usuarioId, 
        @Param("actividadId") UUID actividadId
    );

    @Query(
        """
            SELECT i.id FROM InscripcionEntity i
            WHERE  i.usuario.id = :usuarioId AND i.actividad.id = :actividadId
        """
    )
    public Optional<UUID> findIdByUsuarioIdAndActividadId(
        @Param("usuarioId") UUID usuarioId , 
        @Param("actividadId") UUID actividadId
    );
    
    @Query("SELECT i FROM InscripcionEntity i WHERE i.estadoInscripcion.id = :estadoId")
    public List<InscripcionEntity> findByEstadoInscripcionId(@Param("estadoId") UUID estadoId);
    
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InscripcionEntity i WHERE i.usuario.id = :usuarioId AND i.actividad.id = :actividadId")
    public boolean existsByUsuarioIdAndActividadId(
        @Param("usuarioId") UUID usuarioId, 
        @Param("actividadId") UUID actividadId
    );
}

