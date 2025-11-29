package com.zentry.sigea.module_informe.services.usecases.tipo_informe;

import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.TipoInformeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarTipoInformeUseCase {

    private final ITipoInformeRepository tipoInformeRepository;
    private final TipoInformeMapper tipoInformeMapper;

    public ListarTipoInformeUseCase(ITipoInformeRepository tipoInformeRepository, TipoInformeMapper tipoInformeMapper) {
        this.tipoInformeRepository = tipoInformeRepository;
        this.tipoInformeMapper = tipoInformeMapper;
    }

    /**
     * Lista todos los tipos de informe.
     * @return Lista de DTOs de respuesta de tipos de informe.
     */
    public List<TipoInformeResponse> execute() {
        List<TipoInformeDomainEntity> tipos = tipoInformeRepository.findAll();
        return tipos.stream()
                .map(tipoInformeMapper::toResponse)
                .collect(Collectors.toList());
    }
}
