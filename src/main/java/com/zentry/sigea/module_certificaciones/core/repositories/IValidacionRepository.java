package com.zentry.sigea.module_certificaciones.core.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;

/**
 * Repositorio del dominio para la gestión de validaciones
 */
public interface IValidacionRepository {
    /**
     * Guarda una validación en el repositorio
     */
    ValidacionDomainEntity save(ValidacionDomainEntity validacionDomainEntity);
    
    /**
     * Busca una validación por su ID
     */
    Optional<ValidacionDomainEntity> findById(String id);
    
    /**
     * Busca todas las validaciones de un certificado específico
     */
    List<ValidacionDomainEntity> findByCertificadoId(String certificadoId);
    
    /**
     * Busca validaciones por tipo de validador
     */
    List<ValidacionDomainEntity> findByTipoValidadorId(String tipoValidadorId);
    
    /**
     * Busca una validación específica por certificado y tipo de validador
     */
    Optional<ValidacionDomainEntity> findByCertificadoIdAndTipoValidadorId(String certificadoId, UUID tipoValidadorId);
    
    /**
     * Busca validaciones por resultado (APROBADO/RECHAZADO)
     */
    List<ValidacionDomainEntity> findByResultado(String resultado);
    
    /**
     * Obtiene todas las validaciones
     */
    List<ValidacionDomainEntity> findAll();
    
    /**
     * Verifica si existe una validación para un certificado y tipo de validador específicos
     */
    boolean existsByCertificadoIdAndTipoValidadorId(String certificadoId, String tipoValidadorId);
    
    /**
     * Verifica si existe una validación por su ID
     */
    boolean existsById(String id);
    
    /**
     * Elimina una validación por su ID
     */
    void deleteById(String id);
    
    /**
     * Cuenta el número de validaciones de un certificado
     */
    Long countByCertificadoId(String certificadoId);
    
    /**
     * Cuenta el número de validaciones por resultado
     */
    Long countByResultado(String resultado);
}