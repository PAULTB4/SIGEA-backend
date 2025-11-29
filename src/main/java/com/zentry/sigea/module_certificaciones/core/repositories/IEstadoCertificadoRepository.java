package com.zentry.sigea.module_certificaciones.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;

/**
 * Repositorio del dominio para la gestión de estados de certificado
 */
public interface IEstadoCertificadoRepository {
    /**
     * Guarda un estado de certificado en el repositorio
     */
    EstadoCertificadoDomainEntity save(EstadoCertificadoDomainEntity estadoCertificadoDomainEntity);
    
    /**
     * Busca un estado de certificado por su ID
     */
    Optional<EstadoCertificadoDomainEntity> findById(String id);
    
    /**
     * Busca un estado de certificado por su código
     */
    Optional<EstadoCertificadoDomainEntity> findByCodigo(String codigo);
    
    /**
     * Obtiene todos los estados de certificado
     */
    List<EstadoCertificadoDomainEntity> findAll();
    
    /**
     * Verifica si existe un estado con un código específico
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Verifica si existe un estado por su ID
     */
    boolean existsById(Long id);
    
    /**
     * Elimina un estado de certificado por su ID
     */
    void deleteById(String id);
}