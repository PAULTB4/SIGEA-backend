package com.zentry.sigea.module_asistencias.services.usecases;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;

/**
 * Caso de uso para registrar la asistencia de un participante a una sesión
 */
@Component
public class RegistrarAsistenciaUseCase {

    private final IAsistenciaRepository asistenciaRepository;
    private final ISesionRepository sesionRepository;
    private final IInscripcionRepository inscripcionRepository;

    public RegistrarAsistenciaUseCase(
        IAsistenciaRepository asistenciaRepository,
        ISesionRepository sesionRepository,
        IInscripcionRepository inscripcionRepository
    ) {
        this.asistenciaRepository = asistenciaRepository;
        this.sesionRepository = sesionRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    /**
     * Registra la asistencia de un participante
     * Este es un evento de negocio que debe validar:
     * 1. Que la sesión existe y está activa
     * 2. Que la inscripción es válida
     * 3. Que no se haya registrado previamente 
     */
    public String execute(RegistrarAsistenciaRequest request) {
        validateRequest(request);
        
        if (!sesionRepository.existsById(request.getSesionId())) {
            throw new IllegalArgumentException(
                "No se encontró una sesión con ID: " + request.getSesionId()
            );
        }
        
        if (!inscripcionRepository.existsById(request.getInscripcionId())) {
            throw new IllegalArgumentException(
                "No se encontró una inscripción con ID: " + request.getInscripcionId()
            );
        }
        validateBusinessRules(request);
        
        AsistenciaDomainEntity asistenciaRegistrada = AsistenciaDomainEntity.create(
            request.getSesionId(),
            request.getInscripcionId(),
            request.getPresente()
        );
        asistenciaRepository.save(asistenciaRegistrada);
        
        return String.format(
            "Asistencia registrada: Participante %s en sesión %s",
            asistenciaRegistrada.estaPresente() ? "PRESENTE" : "AUSENTE",
            request.getSesionId()
        );
    }

    private void validateRequest(RegistrarAsistenciaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El request no puede ser nulo");
        }
        if (request.getSesionId() == null || request.getSesionId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de sesión es obligatorio");
        }
        if (request.getInscripcionId() == null || request.getInscripcionId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de inscripción es obligatorio");
        }
        if (request.getPresente() == null) {
            throw new IllegalArgumentException("Debe especificar si está presente");
        }
    }

    private void validateBusinessRules(RegistrarAsistenciaRequest request) {

    }
}