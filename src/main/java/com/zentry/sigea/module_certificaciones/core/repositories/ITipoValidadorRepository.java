package com.zentry.sigea.module_certificaciones.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_certificaciones.core.entities.TipoValidadorDomainEntity;

/**
 * Repositorio del dominio para la gestión de tipos de validador
 */
public interface ITipoValidadorRepository {
    /**
     * Guarda un tipo de validador en el repositorio
     */
    TipoValidadorDomainEntity save(TipoValidadorDomainEntity tipoValidadorDomainEntity);
    
    /**
     * Busca un tipo de validador por su ID
     */
    Optional<TipoValidadorDomainEntity> findById(String id);
    
    /**
     * Busca un tipo de validador por su código
     */
    Optional<TipoValidadorDomainEntity> findByCodigo(String codigo);
    
    /**
     * Obtiene todos los tipos de validador
     */
    List<TipoValidadorDomainEntity> findAll();
    
    /**
     * Busca tipos de validador que requieren conexión en línea
     */
    List<TipoValidadorDomainEntity> findByRequiereConexion(boolean requiereConexion);
    
    /**
     * Busca tipos de validador criptográficos
     */
    List<TipoValidadorDomainEntity> findByCriptografico(boolean esCriptografico);
    
    /**
     * Verifica si existe un tipo de validador con un código específico
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Verifica si existe un tipo de validador por su ID
     */
    boolean existsById(String id);
    
    /**
     * Elimina un tipo de validador por su ID
     */
    void deleteById(String id);
}