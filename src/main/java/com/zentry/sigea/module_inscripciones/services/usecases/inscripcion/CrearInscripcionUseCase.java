package com.zentry.sigea.module_inscripciones.services.usecases.inscripcion;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_inscripciones.core.repositories.IEstadoInscripcionRepository;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;

/**
 * Caso de uso para crear una nueva inscripción
 */
@Component
public class CrearInscripcionUseCase {

    private final IInscripcionRepository inscripcionRepository;
    private final IEstadoInscripcionRepository estadoInscripcionRepository;

    public CrearInscripcionUseCase(
        IInscripcionRepository inscripcionRepository,
        IEstadoInscripcionRepository estadoInscripcionRepository
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.estadoInscripcionRepository = estadoInscripcionRepository;
    }

    /**
     * Ejecuta la creación de inscripción recibiendo IDs y convirtiéndolos a objetos completos
     */
    public String execute(CrearInscripcionServiceDTO crearInscripcionServiceDTO) {
        // Validar que no exista ya una inscripción para este usuario y actividad
        if (inscripcionRepository.existsByUsuarioIdAndActividadId(
            crearInscripcionServiceDTO.getUsuarioId(), 
            crearInscripcionServiceDTO.getActividadId()
        )) {
            throw new IllegalArgumentException(
                "El usuario ya está inscrito en esta actividad"
            );
        }
        
        // Obtener estado por ID y validar que exista
        EstadoInscripcionDomainEntity estado = getEstadoInscripcionById(crearInscripcionServiceDTO.getEstadoId());
        
        // Validaciones de negocio específicas del caso de uso
        validateBusinessRules(crearInscripcionServiceDTO);
        
        // Crear la entidad usando el factory method del dominio
        LocalDate fechaInscripcion = crearInscripcionServiceDTO.getFechaInscripcion() != null ? 
            crearInscripcionServiceDTO.getFechaInscripcion() : LocalDate.now();
        
        InscripcionDomainEntity nuevaInscripcion = InscripcionDomainEntity.create(
            fechaInscripcion,
            crearInscripcionServiceDTO.getUsuarioId(),
            crearInscripcionServiceDTO.getActividadId(),
            estado
        );
        
        // Guardar usando el repositorio
        return inscripcionRepository.save(nuevaInscripcion) ? 
            "Inscripción registrada con éxito" : 
            "Algo salió mal al guardar la inscripción";
    }

    /**
     * Obtiene un EstadoInscripcion por su ID
     */
    private EstadoInscripcionDomainEntity getEstadoInscripcionById(String estadoId) {
        if (estadoId == null) {
            throw new IllegalArgumentException("El ID del estado de inscripción es obligatorio");
        }

        EstadoInscripcionDomainEntity estado = estadoInscripcionRepository.findById(estadoId).orElse(null);
        if (estado == null) {
            throw new IllegalArgumentException(
                "No se encontró un estado de inscripción con ID: " + estadoId
            );
        }

        return estado;
    }
    
    private void validateBusinessRules(CrearInscripcionServiceDTO crearInscripcionServiceDTO) {
        // Validar que la fecha de inscripción no sea en el pasado (si se proporciona)
        if (crearInscripcionServiceDTO.getFechaInscripcion().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                "La fecha de inscripción no puede ser en el pasado"
            );
        }
    }
}

