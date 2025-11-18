package com.zentry.sigea.module_asistencias.services.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;

 /**
 * Caso de uso para listar asistencias con diferentes filtros
 */
@Component
public class ListarAsistenciasUseCase {
    
    private final IAsistenciaRepository asistenciaRepository;

    public ListarAsistenciasUseCase(IAsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    public List<AsistenciaDomainEntity> executeBySesion(String sesionId) {
        if (sesionId == null || sesionId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de sesión es obligatorio");
        }
        return asistenciaRepository.findBySesionId(sesionId);
    }

    public List<AsistenciaDomainEntity> executeByInscripcion(String inscripcionId) {
        if (inscripcionId == null || inscripcionId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de inscripción es obligatorio");
        }
        return asistenciaRepository.findByInscripcionId(inscripcionId);
    }

    public List<AsistenciaDomainEntity> executeBySesionYEstado(String sesionId, Boolean presente) {
        if (sesionId == null || sesionId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de sesión es obligatorio");
        }
        if (presente == null) {
            throw new IllegalArgumentException("El estado de presencia es obligatorio");
        }
        return asistenciaRepository.findBySesionIdAndPresente(sesionId, presente);
    }
}