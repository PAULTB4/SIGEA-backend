package com.zentry.sigea.module_informe.services.usecases.informe;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.IInformeRepository;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.InformeResponseMapper;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.InformeResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ObtenerInformeUseCase {

    private final IInformeRepository informeRepository;

    public ObtenerInformeUseCase(IInformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    /**
     * Obtiene un informe por su ID.
     * @param idInforme ID del informe a buscar.
     * @return DTO de respuesta del informe encontrado.
     */
    public InformeResponse execute(String idInforme) {
        if (idInforme == null || idInforme.isBlank()) {
            throw new IllegalArgumentException("El ID del informe es obligatorio");
        }
        UUID informeId = UUID.fromString(idInforme);
        Optional<InformeDomainEntity> optionalInforme = informeRepository.findById(informeId);

        if (optionalInforme.isEmpty()) {
            throw new IllegalArgumentException("Informe no encontrado");
        }

        InformeDomainEntity informe = optionalInforme.get();
        return InformeResponseMapper.toResponse(informe);
    }
}
