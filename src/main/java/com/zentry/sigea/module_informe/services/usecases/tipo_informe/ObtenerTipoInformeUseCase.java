package com.zentry.sigea.module_informe.services.usecases.tipo_informe;
import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObtenerTipoInformeUseCase {

    private final ITipoInformeRepository tipoInformeRepository;

    public ObtenerTipoInformeUseCase(ITipoInformeRepository tipoInformeRepository) {
        this.tipoInformeRepository = tipoInformeRepository;
    }

    /**
     * Obtiene un tipo de informe por su ID.
     * @param idTipoInforme ID del tipo de informe a buscar.
     * @return DTO de respuesta del tipo de informe encontrado.
     */
    public TipoInformeResponse execute(String idTipoInforme) {
        if (idTipoInforme == null || idTipoInforme.isBlank()) {
            throw new IllegalArgumentException("El ID del tipo de informe es obligatorio");
        }
        Optional<TipoInformeDomainEntity> optionalTipo = tipoInformeRepository.findById(idTipoInforme);

        if (optionalTipo.isEmpty()) {
            throw new IllegalArgumentException("Tipo de informe no encontrado");
        }

        TipoInformeDomainEntity tipo = optionalTipo.get();
        return new TipoInformeResponse(
            tipo.getCodigo(),
            tipo.getEtiqueta()
        );
    }
}
