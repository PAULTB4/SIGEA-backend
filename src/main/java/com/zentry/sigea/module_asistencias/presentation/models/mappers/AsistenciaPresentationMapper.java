package com.zentry.sigea.module_asistencias.presentation.models.mappers;

import java.util.Optional;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;

public class AsistenciaPresentationMapper {
    public static AsistenciaDomainEntity requestToDomain(RegistrarAsistenciaRequest request) {
        if (request == null) {
            return null;
        }

        return AsistenciaDomainEntity.create(
            request.getSesionId(),
            request.getInscripcionId(),
            request.getPresente() , 
            Optional.of(null)
        );
    }

    public static AsistenciaResponse domainToResponse(AsistenciaDomainEntity domain) {
        if (domain == null) {
            return null;
        }

        return new AsistenciaResponse(
            domain.getId(),
            domain.getSesionId(),
            domain.getInscripcionId(),
            domain.getPresente(),
            domain.getRegistradoEn()
        );
    }
}