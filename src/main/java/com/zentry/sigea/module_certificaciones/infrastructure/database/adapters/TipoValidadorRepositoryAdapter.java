package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.TipoValidadorDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ITipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.TipoValidadorRepository;

/**
 * Adaptador que implementa la interfaz del dominio ITipoValidadorRepository
 * y delega las operaciones al repositorio JPA de infraestructura
 */
@Component
public class TipoValidadorRepositoryAdapter implements ITipoValidadorRepository {
    
    private final TipoValidadorRepository jpaRepository;
    
    public TipoValidadorRepositoryAdapter(TipoValidadorRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public TipoValidadorDomainEntity save(TipoValidadorDomainEntity tipoValidadorDomainEntity) {
        TipoValidadorEntity entity = convertToEntity(tipoValidadorDomainEntity);
        TipoValidadorEntity savedEntity = jpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }
    
    @Override
    public Optional<TipoValidadorDomainEntity> findById(String id) {
        UUID uuid = UUID.fromString(id);
        return jpaRepository.findById(uuid)
            .map(this::convertToDomain);
    }
    
    @Override
    public Optional<TipoValidadorDomainEntity> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo)
            .map(this::convertToDomain);
    }
    
    @Override
    public List<TipoValidadorDomainEntity> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TipoValidadorDomainEntity> findByRequiereConexion(boolean requiereConexion) {
        // TODO: Implementar cuando se agregue el campo en la entidad
        return List.of();
    }
    
    @Override
    public List<TipoValidadorDomainEntity> findByCriptografico(boolean esCriptografico) {
        // TODO: Implementar cuando se agregue el campo en la entidad
        return List.of();
    }
    
    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
    
    @Override
    public boolean existsById(String id) {
        UUID uuid = UUID.fromString(id);
        return jpaRepository.existsById(uuid);
    }
    
    @Override
    public void deleteById(String id) {
        UUID uuid = UUID.fromString(id);
        jpaRepository.deleteById(uuid);
    }
    
    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    private TipoValidadorDomainEntity convertToDomain(TipoValidadorEntity entity) {
        return new TipoValidadorDomainEntity(
            entity.getCodigo(),
            entity.getEtiqueta()
        );
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    private TipoValidadorEntity convertToEntity(TipoValidadorDomainEntity domain) {
        TipoValidadorEntity entity = new TipoValidadorEntity();
        entity.setCodigo(domain.getCodigo());
        entity.setEtiqueta(domain.getEtiqueta());
        return entity;
    }
}