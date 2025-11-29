package com.zentry.sigea.module_informe.services.usecases.tipo_informe;

import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.TipoInformeMapper;
import com.zentry.sigea.module_informe.presentation.models.requestDTO.TipoInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CrearTipoInformeUseCase {

    private final ITipoInformeRepository tipoInformeRepository;
    private final TipoInformeMapper tipoInformeMapper;

    public CrearTipoInformeUseCase(ITipoInformeRepository tipoInformeRepository, TipoInformeMapper tipoInformeMapper) {
        this.tipoInformeRepository = tipoInformeRepository;
        this.tipoInformeMapper = tipoInformeMapper;
    }

    /**
     * Crea un nuevo tipo de informe.
     * @param request DTO con los datos del tipo de informe a crear.
     * @return DTO de respuesta con los datos del tipo de informe creado.
     */
    public TipoInformeResponse execute(TipoInformeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El request no puede ser nulo");
        }
        if (!StringUtils.hasText(request.getCodigo())) {
            throw new IllegalArgumentException("El código es obligatorio");
        }

        // Validar unicidad del código
        Optional<TipoInformeDomainEntity> existente = tipoInformeRepository.findByCodigo(request.getCodigo());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un tipo de informe con ese código");
        }

        TipoInformeDomainEntity tipoInforme = new TipoInformeDomainEntity(
            null,
            request.getCodigo(),
            request.getEtiqueta()
        );

        TipoInformeDomainEntity saved = tipoInformeRepository.save(tipoInforme);

        return tipoInformeMapper.toResponse(saved);
    }

    public TipoInformeDomainEntity execute(TipoInformeDomainEntity domainEntity) {
        return tipoInformeRepository.save(domainEntity);
    }
}
