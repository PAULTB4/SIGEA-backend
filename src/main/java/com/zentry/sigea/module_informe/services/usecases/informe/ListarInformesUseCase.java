package com.zentry.sigea.module_informe.services.usecases.informe;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.IInformeRepository;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.InformeResponseMapper;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.InformeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarInformesUseCase {

    private final IInformeRepository informeRepository;

    public ListarInformesUseCase(IInformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    /**
     * Lista todos los informes.
     * @return Lista de DTOs de respuesta de informes.
     */
    public List<InformeResponse> execute() {
        List<InformeDomainEntity> informes = informeRepository.findAll();
        return informes.stream()
                .map(InformeResponseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
