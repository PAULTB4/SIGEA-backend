package com.zentry.sigea.module_informe.services.usecases.informe;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.IInformeRepository;
import com.zentry.sigea.module_informe.presentation.models.requestDTO.ActualizarInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.InformeResponse;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.InformeResponseMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActualizarInformeUseCase {

    private final IInformeRepository informeRepository;

    public ActualizarInformeUseCase(IInformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    /**
     * Actualiza un informe existente.
     * @param idInforme ID del informe a actualizar.
     * @param request DTO con los nuevos datos del informe.
     * @return DTO de respuesta con los datos actualizados.
     */
    public InformeResponse execute(String idInforme, ActualizarInformeRequest request) {
        if (!StringUtils.hasText(idInforme)) {
            throw new IllegalArgumentException("El ID del informe es obligatorio");
        }

        UUID informeId = UUID.fromString(idInforme);
        Optional<InformeDomainEntity> optionalInforme = informeRepository.findById(informeId);

        if (optionalInforme.isEmpty()) {
            throw new IllegalArgumentException("Informe no encontrado");
        }

        InformeDomainEntity informe = optionalInforme.get();

        if (StringUtils.hasText(request.getArchivoUrl())) {
            informe.setArchivoUrl(request.getArchivoUrl());
        }
        if (request.getFechaSubida() != null) {
            informe.setFechaSubida(request.getFechaSubida());
        }
        if (StringUtils.hasText(request.getObservaciones())) {
            informe.setObservaciones(request.getObservaciones());
        }
        InformeDomainEntity updated = informeRepository.save(informe);
        return InformeResponseMapper.toResponse(updated);
    }
}
