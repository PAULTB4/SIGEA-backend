package com.zentry.sigea.module_certificaciones.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;

/**
 * Repositorio del dominio para la gestión de certificados
 */
public interface ICertificadoRepository {
    /**
     * Guarda un certificado en el repositorio
     */
    CertificadoDomainEntity save(CertificadoDomainEntity certificadoDomainEntity);
    
    /**
     * Busca un certificado por su ID
     */
    Optional<CertificadoDomainEntity> findById(String id);
    
    /**
     * Busca un certificado por su código de validación
     */
    Optional<CertificadoDomainEntity> findByCodigoValidacion(String codigoValidacion);
    
    /**
     * Busca un certificado por la asistencia ID
     */
    Optional<CertificadoDomainEntity> findByAsistenciaId(String asistenciaId);
    
    /**
     * Busca todos los certificados con un estado específico
     */
    List<CertificadoDomainEntity> findByEstadoId(String estadoId);
    
    /**
     * Obtiene todos los certificados
     */
    List<CertificadoDomainEntity> findAll();
    
    /**
     * Verifica si existe un certificado para una asistencia específica
     */
    boolean existsByAsistenciaId(String asistenciaId);
    
    /**
     * Verifica si existe un certificado con un código de validación específico
     */
    boolean existsByCodigoValidacion(String codigoValidacion);
    
    /**
     * Verifica si existe un certificado por su ID
     */
    boolean existsById(String id);
    
    /**
     * Elimina un certificado por su ID
     */
    void deleteById(String id);
    
    /**
     * Cuenta el número total de certificados
     */
    Long count();
    
    /**
     * Cuenta el número de certificados por estado
     */
    Long countByEstadoId(Long estadoId);
}