package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.EstadoCertificadoRepository;

/**
 * Adaptador que implementa la interfaz del dominio IEstadoCertificadoRepository
 * y delega las operaciones al repositorio JPA de infraestructura
 */
@Component
public class EstadoCertificadoRepositoryAdapter implements IEstadoCertificadoRepository {
    
    private final EstadoCertificadoRepository jpaRepository;
    
    public EstadoCertificadoRepositoryAdapter(EstadoCertificadoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public EstadoCertificadoDomainEntity save(EstadoCertificadoDomainEntity estadoCertificadoDomainEntity) {
        EstadoCertificadoEntity entity = convertToEntity(estadoCertificadoDomainEntity);
        EstadoCertificadoEntity savedEntity = jpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }
    
    @Override
    public Optional<EstadoCertificadoDomainEntity> findById(String id) {
        UUID uuid = UUID.fromString(id);
        return jpaRepository.findById(uuid)
            .map(this::convertToDomain);
    }
    
    @Override
    public Optional<EstadoCertificadoDomainEntity> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo)
            .map(this::convertToDomain);
    }
    
    @Override
    public List<EstadoCertificadoDomainEntity> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
    
    @Override
    public boolean existsById(Long id) {
        UUID uuid = UUID.fromString(id.toString());
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
    private EstadoCertificadoDomainEntity convertToDomain(EstadoCertificadoEntity entity) {
        return new EstadoCertificadoDomainEntity(
            entity.getCodigo(),
            entity.getEtiqueta()
        );
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    private EstadoCertificadoEntity convertToEntity(EstadoCertificadoDomainEntity domain) {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();
        entity.setCodigo(domain.getCodigo());
        entity.setEtiqueta(domain.getEtiqueta());
        return entity;
    }
}