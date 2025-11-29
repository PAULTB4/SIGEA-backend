package com.zentry.sigea.module_certificaciones.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.CertificadoEntity;

@Repository
public interface CertificadoRepository extends JpaRepository<CertificadoEntity, UUID> {
    
    /**
     * Busca un certificado por su código de validación
     * @param codigoValidacion El código único del certificado
     * @return Optional con el certificado encontrado
     */
    Optional<CertificadoEntity> findByCodigoValidacion(String codigoValidacion);
    
    /**
     * Busca certificados por ID de inscripción
     * @param asistenciaId ID de la asistencia
     * @return Optional con el certificado (debería ser único por asistencia)
     */
    Optional<CertificadoEntity> findByAsistenciaId(UUID asistenciaId);
    
    /**
     * Busca certificados por estado
     * @param estadoId ID del estado del certificado
     * @return Lista de certificados con ese estado
     */
    @Query("SELECT c FROM CertificadoEntity c WHERE c.estado.idEstadoCertificado = :estadoId")
    List<CertificadoEntity> findByEstadoId(@Param("estadoId") UUID estadoId);
    
    /**
     * Busca certificados emitidos en un rango de fechas
     * @param fechaInicio Fecha inicio del rango
     * @param fechaFin Fecha fin del rango
     * @return Lista de certificados emitidos en ese rango
     */
    @Query("SELECT c FROM CertificadoEntity c WHERE c.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    List<CertificadoEntity> findByFechaEmisionBetween(@Param("fechaInicio") LocalDate fechaInicio, 
                                                @Param("fechaFin") LocalDate fechaFin);
    
    /**
     * Verifica si existe un certificado para una inscripción
     * @param asistenciaId ID de la asistencia
     * @return true si existe, false caso contrario
     */
    boolean existsByAsistenciaId(UUID asistenciaId);
    
    /**
     * Cuenta certificados por estado
     * @param estadoId ID del estado
     * @return Número de certificados con ese estado
     */
    @Query("SELECT COUNT(c) FROM CertificadoEntity c WHERE c.estado.idEstadoCertificado = :estadoId")
    Long countByEstadoId(@Param("estadoId") UUID estadoId);
}