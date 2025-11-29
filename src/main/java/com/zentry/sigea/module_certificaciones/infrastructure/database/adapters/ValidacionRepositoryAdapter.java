package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.IValidacionRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.ValidacionEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.TipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.ValidacionRepository;

/**
 * Adaptador que implementa la interfaz del dominio IValidacionRepository
 * y delega las operaciones al repositorio JPA de infraestructura
 */
@Component
public class ValidacionRepositoryAdapter implements IValidacionRepository {
    
    private final ValidacionRepository jpaRepository;
    private final TipoValidadorRepository tipoValidadorRepository;
    
    public ValidacionRepositoryAdapter(
        ValidacionRepository jpaRepository , 
        TipoValidadorRepository tipoValidadorRepository
    ) {
        this.jpaRepository = jpaRepository;
        this.tipoValidadorRepository = tipoValidadorRepository;
    }
    
    @Override
    public ValidacionDomainEntity save(ValidacionDomainEntity validacionDomainEntity) {
        TipoValidadorEntity tipoValidadorEntity = tipoValidadorRepository.findByCodigo(
            validacionDomainEntity.getTipoValidador()
        ).orElse(null);
        
        ValidacionEntity entity = convertToEntity(
            validacionDomainEntity , 
            tipoValidadorEntity
        );

        ValidacionEntity savedEntity = jpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }
    
    @Override
    public Optional<ValidacionDomainEntity> findById(String id) {
        UUID uuid = UUID.fromString(id);
        return jpaRepository.findById(uuid)
            .map(this::convertToDomain);
    }
    
    @Override
    public List<ValidacionDomainEntity> findByCertificadoId(String certificadoId) {
        UUID certificadoUuid = UUID.fromString(certificadoId);
        return jpaRepository.findByCertificadoId(certificadoUuid)
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ValidacionDomainEntity> findByTipoValidadorId(String tipoValidadorId) {
        UUID tipoUuid = UUID.fromString(tipoValidadorId);
        return jpaRepository.findByTipoValidadorId(tipoUuid)
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ValidacionDomainEntity> findByCertificadoIdAndTipoValidadorId(String certificadoId, String tipoValidadorId) {
        UUID certificadoUuid = UUID.fromString(certificadoId);
        UUID tipoUuid = UUID.fromString(tipoValidadorId);
        return jpaRepository.findByCertificadoIdAndTipoValidadorId(certificadoUuid, tipoUuid)
            .map(this::convertToDomain);
    }
    
    @Override
    public List<ValidacionDomainEntity> findByResultado(String resultado) {
        return jpaRepository.findByResultado(resultado)
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ValidacionDomainEntity> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByCertificadoIdAndTipoValidadorId(String certificadoId, String tipoValidadorId) {
        UUID certificadoUuid = UUID.fromString(certificadoId);
        UUID tipoUuid = UUID.fromString(tipoValidadorId);
        return jpaRepository.findByCertificadoIdAndTipoValidadorId(certificadoUuid, tipoUuid)
            .isPresent();
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
    
    @Override
    public Long countByCertificadoId(String certificadoId) {
        UUID certificadoUuid = UUID.fromString(certificadoId);
        return jpaRepository.findByCertificadoId(certificadoUuid)
            .stream()
            .count();
    }
    
    @Override
    public Long countByResultado(String resultado) {
        return jpaRepository.findByResultado(resultado)
            .stream()
            .count();
    }
    
    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    private ValidacionDomainEntity convertToDomain(ValidacionEntity entity) {
        ValidacionDomainEntity domain = new ValidacionDomainEntity();

        // Mapear campos básicos
        if (entity.getCertificado() != null) {
            domain.setCertificado(entity.getCertificado().getIdCertificado().toString());
        }
        domain.setTipoValidador(entity.getTipoValidador().getCodigo());
        domain.setFechaValidacion(entity.getFechaValidacion());
        domain.setResultado(entity.getResultado());
        domain.setDetalle(entity.getDetalle());
        
        return domain;
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    private ValidacionEntity convertToEntity(
        ValidacionDomainEntity domain , 
        TipoValidadorEntity tipoValidadorEntity
    ) {
        ValidacionEntity entity = new ValidacionEntity();
        
        // Mapear campos básicos
        // Nota: El certificado y tipo validador deben ser cargados por separado
        entity.setTipoValidador(tipoValidadorEntity);
        entity.setFechaValidacion(domain.getFechaValidacion());
        entity.setResultado(domain.getResultado());
        entity.setDetalle(domain.getDetalle());
        
        // TODO: Cargar certificado por ID cuando sea necesario
        
        return entity;
    }
}