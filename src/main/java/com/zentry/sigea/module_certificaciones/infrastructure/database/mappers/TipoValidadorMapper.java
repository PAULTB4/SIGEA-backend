package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.TipoValidadorDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;

/**
 * Mapper para convertir entre entidades de dominio e infraestructura de TipoValidador
 */
@Component
public class TipoValidadorMapper {

    /**
     * Convierte de entidad de infraestructura a entidad de dominio
     */
    public TipoValidadorDomainEntity toDomain(TipoValidadorEntity tipoValidador) {
        if (tipoValidador == null) {
            return null;
        }

        TipoValidadorDomainEntity domainEntity = new TipoValidadorDomainEntity();
        domainEntity.setCodigo(tipoValidador.getCodigo());
        domainEntity.setEtiqueta(tipoValidador.getEtiqueta());

        return domainEntity;
    }

    /**
     * Convierte de entidad de dominio a entidad de infraestructura
     */
    public TipoValidadorEntity toInfrastructure(TipoValidadorDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        TipoValidadorEntity tipoValidador = new TipoValidadorEntity();
        tipoValidador.setCodigo(domainEntity.getCodigo());
        tipoValidador.setEtiqueta(domainEntity.getEtiqueta());

        return tipoValidador;
    }

    /**
     * Actualiza una entidad de infraestructura con los datos de dominio
     */
    public void updateInfrastructure(TipoValidadorEntity tipoValidador, TipoValidadorDomainEntity domainEntity) {
        if (tipoValidador == null || domainEntity == null) {
            return;
        }

        tipoValidador.setCodigo(domainEntity.getCodigo());
        tipoValidador.setEtiqueta(domainEntity.getEtiqueta());
    }
}