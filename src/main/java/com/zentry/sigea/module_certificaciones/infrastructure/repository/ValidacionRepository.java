package com.zentry.sigea.module_certificaciones.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.ValidacionEntity;

@Repository
public interface ValidacionRepository extends JpaRepository<ValidacionEntity, UUID> {
    
    /**
     * Busca validaciones por certificado
     * @param certificadoId ID del certificado
     * @return Lista de validaciones del certificado
     */
    @Query("SELECT v FROM ValidacionEntity v WHERE v.certificado.idCertificado = :certificadoId")
    List<ValidacionEntity> findByCertificadoId(@Param("certificadoId") UUID certificadoId);
    
    /**
     * Busca validaciones por tipo de validador
     * @param tipoValidadorId ID del tipo de validador
     * @return Lista de validaciones de ese tipo
     */
    @Query("SELECT v FROM ValidacionEntity v WHERE v.tipoValidador.idTipoValidador = :tipoValidadorId")
    List<ValidacionEntity> findByTipoValidadorId(@Param("tipoValidadorId") UUID tipoValidadorId);
    
    /**
     * Busca validación específica por certificado y tipo
     * @param certificadoId ID del certificado
     * @param tipoValidadorId ID del tipo de validador
     * @return Optional con la validación encontrada
     */
    @Query("SELECT v FROM ValidacionEntity v WHERE v.certificado.idCertificado = :certificadoId " +
           "AND v.tipoValidador.idTipoValidador = :tipoValidadorId")
    Optional<ValidacionEntity> findByCertificadoIdAndTipoValidadorId(
            @Param("certificadoId") UUID certificadoId,
            @Param("tipoValidadorId") UUID tipoValidadorId);
    
    /**
     * Busca validaciones por resultado
     * @param resultado Resultado de la validación (APROBADO, RECHAZADO)
     * @return Lista de validaciones con ese resultado
     */
    List<ValidacionEntity> findByResultado(String resultado);
    
    /**
     * Busca validaciones en un rango de fechas
     * @param fechaInicio Fecha inicio del rango
     * @param fechaFin Fecha fin del rango
     * @return Lista de validaciones en ese rango
     */
    @Query("SELECT v FROM ValidacionEntity v WHERE v.fechaValidacion BETWEEN :fechaInicio AND :fechaFin")
    List<ValidacionEntity> findByFechaValidacionBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                                  @Param("fechaFin") LocalDate fechaFin);
    
    /**
     * Cuenta validaciones aprobadas para un certificado
     * @param certificadoId ID del certificado
     * @return Número de validaciones aprobadas
     */
    @Query("SELECT COUNT(v) FROM ValidacionEntity v WHERE v.certificado.idCertificado = :certificadoId " +
           "AND v.resultado = 'APROBADO'")
    Long countValidacionesAprobadasByCertificado(@Param("certificadoId") UUID certificadoId);
}