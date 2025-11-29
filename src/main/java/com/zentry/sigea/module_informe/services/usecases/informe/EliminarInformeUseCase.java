package com.zentry.sigea.module_informe.services.usecases.informe;

import com.zentry.sigea.module_informe.core.repositories.IInformeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EliminarInformeUseCase {

    private final IInformeRepository informeRepository;

    public EliminarInformeUseCase(IInformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    /**
     * Elimina un informe por su ID.
     * @param idInforme ID del informe a eliminar.
     */
    public void execute(String idInforme) {
        if (idInforme == null || idInforme.isBlank()) {
            throw new IllegalArgumentException("El ID del informe es obligatorio");
        }
        UUID informeId = UUID.fromString(idInforme);
        informeRepository.deleteById(informeId);
    }
}
