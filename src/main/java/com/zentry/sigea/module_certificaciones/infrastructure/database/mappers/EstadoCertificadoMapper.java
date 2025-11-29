package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;

/**
 * Mapper para convertir entre entidades de dominio e infraestructura de EstadoCertificado
 */
@Component
public class EstadoCertificadoMapper {

    /**
     * Convierte de entidad de infraestructura a entidad de dominio
     */
    public EstadoCertificadoDomainEntity toDomain(EstadoCertificadoEntity estado) {
        if (estado == null) {
            return null;
        }

        EstadoCertificadoDomainEntity domainEntity = new EstadoCertificadoDomainEntity();
        domainEntity.setCodigo(estado.getCodigo());
        domainEntity.setEtiqueta(estado.getEtiqueta());

        return domainEntity;
    }

    /**
     * Convierte de entidad de dominio a entidad de infraestructura
     */
    public EstadoCertificadoEntity toInfrastructure(EstadoCertificadoDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        EstadoCertificadoEntity estado = new EstadoCertificadoEntity();
        estado.setCodigo(domainEntity.getCodigo());
        estado.setEtiqueta(domainEntity.getEtiqueta());

        return estado;
    }

    /**
     * Actualiza una entidad de infraestructura con los datos de dominio
     */
    public void updateInfrastructure(EstadoCertificadoEntity estado, EstadoCertificadoDomainEntity domainEntity) {
        if (estado == null || domainEntity == null) {
            return;
        }

        estado.setCodigo(domainEntity.getCodigo());
        estado.setEtiqueta(domainEntity.getEtiqueta());
    }
}