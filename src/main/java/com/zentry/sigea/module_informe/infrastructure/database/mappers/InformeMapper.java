package com.zentry.sigea.module_informe.infrastructure.database.mappers;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.infrastructure.database.entities.InformeEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;

import java.util.UUID;

public class InformeMapper {

    // Domain → Entity
    public static InformeEntity toEntity(
            InformeDomainEntity informeDomainEntity,
            ActividadEntity actividadEntity,
            TipoInformeEntity tipoInformeEntity
    ) {
        if (informeDomainEntity == null) return null;

        InformeEntity informeEntity = new InformeEntity();

        if (informeDomainEntity.getId() != null) {
            informeEntity.setId(UUID.fromString(informeDomainEntity.getId()));
        }
        informeEntity.setActividad(actividadEntity);
        informeEntity.setTipoInforme(tipoInformeEntity);
        informeEntity.setFechaSubida(informeDomainEntity.getFechaSubida());
        informeEntity.setArchivoUrl(informeDomainEntity.getArchivoUrl());
        informeEntity.setObservaciones(informeDomainEntity.getObservaciones());
        informeEntity.setCreatedAt(informeDomainEntity.getCreatedAt());
        informeEntity.setUpdatedAt(informeDomainEntity.getUpdatedAt());

        return informeEntity;
    }

    // Entity → Domain
    public static InformeDomainEntity toDomain(InformeEntity informeEntity) {
        if (informeEntity == null) return null;

        return new InformeDomainEntity(
            informeEntity.getId() != null ? informeEntity.getId().toString() : null,
            informeEntity.getActividad() != null ? informeEntity.getActividad().getId().toString() : null,
            informeEntity.getTipoInforme() != null ? informeEntity.getTipoInforme().getId().toString() : null,
            informeEntity.getFechaSubida(),
            informeEntity.getArchivoUrl(),
            informeEntity.getObservaciones(),
            informeEntity.getCreatedAt(),
            informeEntity.getUpdatedAt(),
            // Nuevos campos:
            informeEntity.getActividad() != null ? informeEntity.getActividad().getTitulo() : null,
            informeEntity.getTipoInforme() != null ? informeEntity.getTipoInforme().getEtiqueta() : null
        );
    }
}
