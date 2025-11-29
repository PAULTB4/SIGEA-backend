package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.CertificadoEntity;

/**
 * Mapper para convertir entre entidades de dominio e infraestructura de Certificado
 */
@Component
public class CertificadoMapper {

    private final EstadoCertificadoMapper estadoMapper;

    public CertificadoMapper(EstadoCertificadoMapper estadoMapper) {
        this.estadoMapper = estadoMapper;
    }

    /**
     * Convierte de entidad de infraestructura a entidad de dominio
     */
    public CertificadoDomainEntity toDomain(CertificadoEntity certificado) {
        if (certificado == null) {
            return null;
        }

        CertificadoDomainEntity domainEntity = new CertificadoDomainEntity();
        domainEntity.setAsistenciaId(certificado.getAsistenciaId().toString()); // Mapeo del nombre inconsistente
        domainEntity.setCodigoValidacion(certificado.getCodigoValidacion());
        domainEntity.setFechaEmision(certificado.getFechaEmision());
        domainEntity.setEstado(estadoMapper.toDomain(certificado.getEstado()));
        domainEntity.setUrlPdf(certificado.getUrlPdf());
        domainEntity.setCreatedAt(certificado.getCreatedAt());
        domainEntity.setUpdatedAt(certificado.getUpdatedAt());

        return domainEntity;
    }

    /**
     * Convierte de entidad de dominio a entidad de infraestructura
     */
    public CertificadoEntity toInfrastructure(CertificadoDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        CertificadoEntity certificado = new CertificadoEntity();
        certificado.setAsistenciaId(UUID.fromString(domainEntity.getAsistenciaId())); // Mapeo del nombre inconsistente
        certificado.setCodigoValidacion(domainEntity.getCodigoValidacion());
        certificado.setFechaEmision(domainEntity.getFechaEmision());
        certificado.setEstado(estadoMapper.toInfrastructure(domainEntity.getEstado()));
        certificado.setUrlPdf(domainEntity.getUrlPdf());
        certificado.setCreatedAt(domainEntity.getCreatedAt());
        certificado.setUpdatedAt(domainEntity.getUpdatedAt());

        return certificado;
    }

    /**
     * Actualiza una entidad de infraestructura con los datos de dominio
     */
    public void updateInfrastructure(CertificadoEntity certificado, CertificadoDomainEntity domainEntity) {
        if (certificado == null || domainEntity == null) {
            return;
        }

        certificado.setAsistenciaId(UUID.fromString(domainEntity.getAsistenciaId()));
        certificado.setCodigoValidacion(domainEntity.getCodigoValidacion());
        certificado.setFechaEmision(domainEntity.getFechaEmision());
        certificado.setEstado(estadoMapper.toInfrastructure(domainEntity.getEstado()));
        certificado.setUrlPdf(domainEntity.getUrlPdf());
        certificado.setUpdatedAt(domainEntity.getUpdatedAt());
    }
}