package com.zentry.sigea.module_informe.config;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import com.zentry.sigea.module_informe.presentation.models.requestDTO.CrearInformeRequest;
import org.springframework.stereotype.Component;

@Component
public class InformeMappingConfig {

    private final ITipoInformeRepository tipoInformeRepository;

    public InformeMappingConfig(ITipoInformeRepository tipoInformeRepository) {
        this.tipoInformeRepository = tipoInformeRepository;
    }

    public InformeDomainEntity mapToDomain(CrearInformeRequest request) {
        // Convertir String a UUID 
        String actividadId = request.getActividadId();
        String tipoInformeId = request.getTipoInformeId();

        // Obtener el tipo de informe
        TipoInformeDomainEntity tipoInforme = tipoInformeRepository.findById(tipoInformeId)
            .orElseThrow(() -> new IllegalArgumentException("Tipo de informe no encontrado"));

        // Crear la entidad de dominio
        return new InformeDomainEntity(
            null,
            actividadId,
            tipoInformeId,
            request.getFechaSubida(),
            null,
            request.getObservaciones(),
            null,
            null,
            null, 
            tipoInforme.getEtiqueta() 
        );
    }
}
