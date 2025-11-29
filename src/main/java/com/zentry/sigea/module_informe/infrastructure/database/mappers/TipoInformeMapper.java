package com.zentry.sigea.module_informe.infrastructure.database.mappers;

import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir dominio y JPA
 */
@Component
public class TipoInformeMapper {

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public static TipoInformeDomainEntity toDomain(TipoInformeEntity tipoInformeEntity) {
        if (tipoInformeEntity == null) {
            return null;
        }

        TipoInformeDomainEntity tipoInformeDomainEntity = new TipoInformeDomainEntity();
        tipoInformeDomainEntity.setId(tipoInformeEntity.getId() != null ? tipoInformeEntity.getId().toString() : null);
        tipoInformeDomainEntity.setCodigo(tipoInformeEntity.getCodigo());
        tipoInformeDomainEntity.setEtiqueta(tipoInformeEntity.getEtiqueta());
        return tipoInformeDomainEntity;
    }

    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public static TipoInformeEntity toEntity(TipoInformeDomainEntity tipoInformeDomainEntity) {
        if (tipoInformeDomainEntity == null) {
            return null;
        }

        TipoInformeEntity tipoInformeEntity = new TipoInformeEntity();
        if (tipoInformeDomainEntity.getId() != null) {
            tipoInformeEntity.setId(java.util.UUID.fromString(tipoInformeDomainEntity.getId()));
        }
        tipoInformeEntity.setCodigo(tipoInformeDomainEntity.getCodigo());
        tipoInformeEntity.setEtiqueta(tipoInformeDomainEntity.getEtiqueta());
        return tipoInformeEntity;
    }

    /**
     * Convierte de entidad de dominio a respuesta REST
     */
    public TipoInformeResponse toResponse(TipoInformeDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        return new TipoInformeResponse(
            domainEntity.getCodigo(),
            domainEntity.getEtiqueta()
        );
    }
}
