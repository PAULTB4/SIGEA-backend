package com.zentry.sigea.module_inscripciones.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.InscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.InscripcionResponse;
import com.zentry.sigea.module_inscripciones.services.interfaces.IInscripcion;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.ActualizarInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.CrearInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.EliminarInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.ObtenerInscripcionPorIdUseCase;
import com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent;

/**
 * Servicio de aplicación que orquesta casos de uso de inscripciones
 * APPLICATION LAYER - Con anotaciones de Spring
 */
@Service
public class InscripcionService implements IInscripcion {

    private final IInscripcionRepository inscripcionRepository;
    private final CrearInscripcionUseCase crearInscripcionUseCase;
    private final ActualizarInscripcionUseCase actualizarInscripcionUseCase;
    private final EliminarInscripcionUseCase eliminarInscripcionUseCase;
    private final ObtenerInscripcionPorIdUseCase obtenerInscripcionUseCase;
    private final ApplicationEventPublisher eventPublisher;

    public InscripcionService(
        IInscripcionRepository inscripcionRepository,
        CrearInscripcionUseCase crearInscripcionUseCase,
        ActualizarInscripcionUseCase actualizarInscripcionUseCase,
        EliminarInscripcionUseCase eliminarInscripcionUseCase,
        ObtenerInscripcionPorIdUseCase obtenerInscripcionUseCase,
        ApplicationEventPublisher eventPublisher
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.crearInscripcionUseCase = crearInscripcionUseCase;
        this.actualizarInscripcionUseCase = actualizarInscripcionUseCase;
        this.eliminarInscripcionUseCase = eliminarInscripcionUseCase;
        this.obtenerInscripcionUseCase = obtenerInscripcionUseCase;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Crea una nueva inscripción usando el request con IDs
     * Publica automáticamente un evento que dispara la notificación
     */
    @Override
    public boolean crearInscripcion(CrearInscripcionServiceDTO crearInscripcionServiceDTO) {


        String inscripcionId = crearInscripcionUseCase.execute(crearInscripcionServiceDTO);
        
        // Publicar evento para notificación automática
        if (inscripcionId == null || inscripcionId.isEmpty()) {
            return false;
        }
        
        eventPublisher.publishEvent(new InscripcionCreadaEvent(
            crearInscripcionServiceDTO.getUsuarioId(),
            crearInscripcionServiceDTO.getActividadId(),
            inscripcionId,
            LocalDateTime.now()
        ));
        
        return true;
    }

    /**
     * Lista todas las inscripciones
     */
    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listarInscripciones() {
        return inscripcionRepository.findAll().stream()
                .map(InscripcionResponse::fromEntity)
                .toList();
    }

    @Override
    public InscripcionResponse actualizarInscripcion(String id, InscripcionRequest request) {
        return actualizarInscripcionUseCase.execute(request, id);
    }

    @Override
    public InscripcionResponse obtenerInscripcionPorId(String id) {
        Optional<InscripcionDomainEntity> inscripcionOpt = obtenerInscripcionUseCase.execute(id);
        if (inscripcionOpt.isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + id);
        }
        return InscripcionResponse.fromEntity(inscripcionOpt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> obtenerInscripcionesPorUsuario(String usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId).stream()
                .map(InscripcionResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> obtenerInscripcionesPorActividad(String actividadId) {
        return inscripcionRepository.findByActividadId(actividadId).stream()
                .map(InscripcionResponse::fromEntity)
                .toList();
    }

    @Override
    public void eliminarInscripcion(String id) {
        eliminarInscripcionUseCase.execute(id);
    }
}

