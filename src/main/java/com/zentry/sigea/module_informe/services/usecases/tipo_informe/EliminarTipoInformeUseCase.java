package com.zentry.sigea.module_informe.services.usecases.tipo_informe;

import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarTipoInformeUseCase {

    private final ITipoInformeRepository tipoInformeRepository;

    public EliminarTipoInformeUseCase(ITipoInformeRepository tipoInformeRepository) {
        this.tipoInformeRepository = tipoInformeRepository;
    }

    /**
     * Elimina un tipo de informe por su ID.
     * @param idTipoInforme ID del tipo de informe a eliminar.
     */
    public void execute(String idTipoInforme) {
        if (idTipoInforme == null || idTipoInforme.isBlank()) {
            throw new IllegalArgumentException("El ID del tipo de informe es obligatorio");
        }
        tipoInformeRepository.deleteById(idTipoInforme);
    }
}
