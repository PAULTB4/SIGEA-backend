package com.zentry.sigea.module_informe.services.usecases.tipo_informe;
import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import com.zentry.sigea.module_informe.presentation.models.requestDTO.TipoInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ActualizarTipoInformeUseCase {

    private final ITipoInformeRepository tipoInformeRepository;

    public ActualizarTipoInformeUseCase(ITipoInformeRepository tipoInformeRepository) {
        this.tipoInformeRepository = tipoInformeRepository;
    }

    /**
     * Actualiza un tipo de informe existente.
     * @param idTipoInforme ID del tipo de informe a actualizar.
     * @param request DTO con los nuevos datos.
     * @return DTO de respuesta con los datos actualizados.
     */
    public TipoInformeResponse execute(String idTipoInforme, TipoInformeRequest request) {
        Optional<TipoInformeDomainEntity> optionalTipo = tipoInformeRepository.findById(idTipoInforme);

        if (optionalTipo.isEmpty()) {
            throw new IllegalArgumentException("Tipo de informe no encontrado");
        }

        TipoInformeDomainEntity tipoInforme = optionalTipo.get();

        // Actualiza campos permitidos
        if (StringUtils.hasText(request.getCodigo())) {
            tipoInforme.setCodigo(request.getCodigo());
        }
        if (StringUtils.hasText(request.getEtiqueta())) {
            tipoInforme.setEtiqueta(request.getEtiqueta());
        }

        TipoInformeDomainEntity updated = tipoInformeRepository.save(tipoInforme);

        return new TipoInformeResponse(
            updated.getCodigo(),
            updated.getEtiqueta()
        );
    }
}
