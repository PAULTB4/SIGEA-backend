package com.zentry.sigea.module_asistencias.services.usecases;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;

 /**
 * Caso de uso para obtener una asistencia por su ID
 */
@Component
public class ObtenerAsistenciaPorIdUseCase {
    
    private final IAsistenciaRepository asistenciaRepository;

    public ObtenerAsistenciaPorIdUseCase(IAsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    public Optional<AsistenciaDomainEntity> execute(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        return asistenciaRepository.findById(id);
    }
}
