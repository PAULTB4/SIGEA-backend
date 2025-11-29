package com.zentry.sigea.module_informe.services;

import com.zentry.sigea.module_informe.presentation.models.requestDTO.TipoInformeRequest;
import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.TipoInformeMapper;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import com.zentry.sigea.module_informe.services.usecases.tipo_informe.*;
import com.zentry.sigea.module_informe.infrastructure.repository.TipoInformeJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoInformeService {

    @Autowired
    private TipoInformeJPARepository tipoInformeRepository;

    @Autowired
    private TipoInformeMapper tipoInformeMapper;

    private final CrearTipoInformeUseCase crearTipoInformeUseCase;
    private final ListarTipoInformeUseCase listarTipoInformeUseCase;
    private final EliminarTipoInformeUseCase eliminarTipoInformeUseCase;

    public TipoInformeService(
        CrearTipoInformeUseCase crearTipoInformeUseCase,
        ListarTipoInformeUseCase listarTipoInformeUseCase,
        EliminarTipoInformeUseCase eliminarTipoInformeUseCase
    ) {
        this.crearTipoInformeUseCase = crearTipoInformeUseCase;
        this.listarTipoInformeUseCase = listarTipoInformeUseCase;
        this.eliminarTipoInformeUseCase = eliminarTipoInformeUseCase;
    }

    public TipoInformeResponse crearTipoInforme(TipoInformeRequest request) {
        return crearTipoInformeUseCase.execute(request);
    }

    public TipoInformeResponse crearTipoInforme(TipoInformeDomainEntity domainEntity) {
        TipoInformeDomainEntity saved = crearTipoInformeUseCase.execute(domainEntity);
        return tipoInformeMapper.toResponse(saved);
    }

    public List<TipoInformeResponse> listarTiposInforme() {
        List<TipoInformeEntity> entities = tipoInformeRepository.findAll();
        return entities.stream()
            .map(TipoInformeResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public void eliminarTipoInforme(String id) {
        eliminarTipoInformeUseCase.execute(id);
    }
}
