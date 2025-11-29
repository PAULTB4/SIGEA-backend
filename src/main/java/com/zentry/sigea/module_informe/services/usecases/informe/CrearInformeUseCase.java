package com.zentry.sigea.module_informe.services.usecases.informe;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.IInformeRepository;
import com.zentry.sigea.module_informe.presentation.models.requestDTO.CrearInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.InformeResponse;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.InformeResponseMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CrearInformeUseCase {

    private final IInformeRepository informeRepository;

    public CrearInformeUseCase(IInformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    /**
     * Ejecuta el caso de uso para crear un informe.
     * @param request DTO con los datos del informe a crear.
     * @return DTO de respuesta con los datos del informe creado.
     */
    public InformeResponse execute(CrearInformeRequest request) {
        // Validación básica
        if (request == null) {
            throw new IllegalArgumentException("El request no puede ser nulo");
        }
        if (!StringUtils.hasText(request.getActividadId())) {
            throw new IllegalArgumentException("El ID de la actividad es obligatorio");
        }
        if (!StringUtils.hasText(request.getTipoInformeId())) {
            throw new IllegalArgumentException("El ID del tipo de informe es obligatorio");
        }
        // Mapeo del request a la entidad de dominio (sin adjuntos)
        InformeDomainEntity informe = new InformeDomainEntity(
            null,
            request.getActividadId(),
            request.getTipoInformeId(),
            request.getFechaSubida(),
            null,
            request.getObservaciones(),
            null,
            null,
            null, 
            null
        );

        // Persistencia
        InformeDomainEntity saved = informeRepository.save(informe);

        return InformeResponseMapper.toResponse(saved);
    }
}