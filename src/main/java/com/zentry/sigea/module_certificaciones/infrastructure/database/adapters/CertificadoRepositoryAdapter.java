package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.CertificadoEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.CertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.EstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;

/**
 * Adaptador que implementa la interfaz del dominio ICertificadoRepository
 * y delega las operaciones al repositorio JPA de infraestructura
 */
@Component

public class CertificadoRepositoryAdapter implements ICertificadoRepository {
    private final CertificadoRepository jpaRepository;
    private final EstadoCertificadoRepository estadoCertificadoRepository;

    public CertificadoRepositoryAdapter(CertificadoRepository jpaRepository, EstadoCertificadoRepository estadoCertificadoRepository) {
        this.jpaRepository = jpaRepository;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
    }
    
    @Override
    public CertificadoDomainEntity save(CertificadoDomainEntity certificadoDomainEntity) {
        CertificadoEntity entity = convertToEntity(certificadoDomainEntity);
        CertificadoEntity savedEntity = jpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }
    
    @Override
    public Optional<CertificadoDomainEntity> findById(String id) {
        UUID uuid = UUID.fromString(id);
        return jpaRepository.findById(uuid)
            .map(this::convertToDomain);
    }
    
    @Override
    public Optional<CertificadoDomainEntity> findByCodigoValidacion(String codigoValidacion) {
        return jpaRepository.findByCodigoValidacion(codigoValidacion)
            .map(this::convertToDomain);
    }
    
    @Override
    public Optional<CertificadoDomainEntity> findByAsistenciaId(String asistenciaId) {
        UUID asistenciaUuid = UUID.fromString(asistenciaId);
        return jpaRepository.findByAsistenciaId(asistenciaUuid)
            .map(this::convertToDomain);
    }
    
    @Override
    public List<CertificadoDomainEntity> findByEstadoId(String estadoId) {
        UUID estadoUuid = UUID.fromString(estadoId);
        return jpaRepository.findByEstadoId(estadoUuid)
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CertificadoDomainEntity> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::convertToDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByAsistenciaId(String asistenciaId) {
        UUID asistenciaUuid = UUID.fromString(asistenciaId);
        return jpaRepository.existsByAsistenciaId(asistenciaUuid);
    }
    
    @Override
    public boolean existsByCodigoValidacion(String codigoValidacion) {
        return jpaRepository.findByCodigoValidacion(codigoValidacion).isPresent();
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
    public Long count() {
        return jpaRepository.count();
    }
    
    @Override
    public Long countByEstadoId(Long estadoId) {
        UUID estadoUuid = UUID.fromString(estadoId.toString());
        return jpaRepository.countByEstadoId(estadoUuid);
    }
    
    /**
     * Convierte una entidad JPA a una entidad de dominio
     */
    private CertificadoDomainEntity convertToDomain(CertificadoEntity entity) {
        // TODO: Implementar el mapper completo según la estructura de CertificadoDomainEntity
        // Por ahora, creamos una conversión básica
        
        CertificadoDomainEntity domain = new CertificadoDomainEntity();
        
        // Mapear campos básicos
        if (entity.getIdCertificado() != null) {
            domain.setIdCertificado(entity.getIdCertificado().toString());
        }
        domain.setCodigoValidacion(entity.getCodigoValidacion());
        domain.setAsistenciaId(entity.getAsistenciaId().toString());
        domain.setFechaEmision(entity.getFechaEmision());
        domain.setUrlPdf(entity.getUrlPdf());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        
        // Mapear estado si existe
        if (entity.getEstado() != null) {
            EstadoCertificadoDomainEntity estadoDomain = new EstadoCertificadoDomainEntity(
                entity.getEstado().getCodigo(),
                entity.getEstado().getEtiqueta()
            );
            domain.setEstado(estadoDomain);
        }
        
        return domain;
    }
    
    /**
     * Convierte una entidad de dominio a una entidad JPA
     */
    private CertificadoEntity convertToEntity(CertificadoDomainEntity domain) {
        CertificadoEntity entity = new CertificadoEntity();
        // Mapear campos básicos
        if (domain.getIdCertificado() != null && !domain.getIdCertificado().trim().isEmpty()) {
            entity.setIdCertificado(UUID.fromString(domain.getIdCertificado()));
        }
        entity.setCodigoValidacion(domain.getCodigoValidacion());
        entity.setAsistenciaId(UUID.fromString(domain.getAsistenciaId()));
        entity.setFechaEmision(domain.getFechaEmision());
        entity.setUrlPdf(domain.getUrlPdf());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        // Mapear estado correctamente usando el repositorio
        if (domain.getEstado() != null && domain.getEstado().getCodigo() != null) {
            EstadoCertificadoEntity estadoEntity = estadoCertificadoRepository.findByCodigo(domain.getEstado().getCodigo())
                .orElseThrow(() -> new IllegalStateException("No se encontró el estado de certificado con código: " + domain.getEstado().getCodigo()));
            entity.setEstado(estadoEntity);
        } else {
            throw new IllegalArgumentException("El estado del certificado no puede ser nulo");
        }

        return entity;
    }
}