package com.zentry.sigea.module_asistencias.services.usecases;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;

 /**
 * Caso de uso para actualizar el estado de una asistencia
 * (Por ejemplo, cambiar de ausente a presente)
 */
@Component
public class ActualizarAsistenciaUseCase {
    
    private final IAsistenciaRepository asistenciaRepository;

    public ActualizarAsistenciaUseCase(IAsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    public AsistenciaDomainEntity execute(String id, Boolean nuevoEstadoPresente) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        if (nuevoEstadoPresente == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }

        AsistenciaDomainEntity asistencia = asistenciaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró asistencia con ID: " + id
            ));

        if (nuevoEstadoPresente) {
            asistencia.marcarPresente();
        } else {
            asistencia.marcarAusente();
        }
        asistenciaRepository.save(asistencia);

        return asistencia;
    }
}
