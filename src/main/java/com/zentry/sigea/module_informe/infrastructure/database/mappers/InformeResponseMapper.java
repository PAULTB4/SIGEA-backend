package com.zentry.sigea.module_informe.infrastructure.database.mappers;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.InformeResponse;

public class InformeResponseMapper {
    public static InformeResponse toResponse(InformeDomainEntity domain) {
        if (domain == null) return null;
        return new InformeResponse(
            domain.getId(),
            domain.getActividadTitulo(),
            domain.getTipoInformeEtiqueta(),
            domain.getFechaSubida(),
            domain.getArchivoUrl(),
            domain.getObservaciones()
        );
    }
}