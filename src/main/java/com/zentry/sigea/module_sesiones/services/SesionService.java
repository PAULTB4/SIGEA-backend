package com.zentry.sigea.module_sesiones.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_notificaciones.events.domain.SesionCreadaEvent;
import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.presentacion.models.CrearSesionRequest;
import com.zentry.sigea.module_sesiones.presentacion.models.SesionRequest;
import com.zentry.sigea.module_sesiones.presentacion.models.SesionResponse;
import com.zentry.sigea.module_sesiones.services.interfaces.ISesionService;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.ActualizarSesionUseCase;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.CrearSesionUseCase;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.EliminarSesionUseCase;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.ListarSesionesUseCase;



/**
 * Servicio de aplicación para Sesiones
 * Orquesta los casos de uso y maneja transacciones
 */
@Service
@Transactional
public class SesionService implements ISesionService {

    private final CrearSesionUseCase crearSesionUseCase;
    private final ListarSesionesUseCase listarSesionesUseCase;
    private final ActualizarSesionUseCase actualizarSesionUseCase;
    private final EliminarSesionUseCase eliminarSesionUseCase;
    private final ApplicationEventPublisher eventPublisher;
    private final IInscripcionRepository inscripcionRepository;

    public SesionService(
        CrearSesionUseCase crearSesionUseCase,
        ListarSesionesUseCase listarSesionesUseCase,
        ActualizarSesionUseCase actualizarSesionUseCase,
        EliminarSesionUseCase eliminarSesionUseCase,
        ApplicationEventPublisher eventPublisher,
        IInscripcionRepository inscripcionRepository
    ) {
        this.crearSesionUseCase = crearSesionUseCase;
        this.listarSesionesUseCase = listarSesionesUseCase;
        this.actualizarSesionUseCase = actualizarSesionUseCase;
        this.eliminarSesionUseCase = eliminarSesionUseCase;
        this.eventPublisher = eventPublisher;
        this.inscripcionRepository = inscripcionRepository;
    }

    @Override
    public SesionResponse crearSesion(CrearSesionRequest request) {
        try {
            // Crear la sesión
            SesionDomainEntity sesionCreada = crearSesionUseCase.execute(request);
            
            if (sesionCreada == null || sesionCreada.getId() == null) {
                throw new IllegalStateException("La sesión no fue creada correctamente - ID es null");
            }
            
            // Obtener usuarios inscritos en la actividad para notificarles
            List<String> usuariosInscritos = inscripcionRepository
                .findByActividadId(request.getActividadId())
                .stream()
                .map(inscripcion -> inscripcion.getUsuarioId())
                .collect(Collectors.toList());
            
            // Solo publicar evento si hay usuarios inscritos
            if (!usuariosInscritos.isEmpty()) {
                eventPublisher.publishEvent(new SesionCreadaEvent(
                    sesionCreada.getId(),
                    request.getActividadId(),
                    sesionCreada.getTitulo(),
                    sesionCreada.getFechaSesion(),
                    usuariosInscritos,
                    LocalDateTime.now()
                ));
            }
            
            return SesionResponse.fromDomain(sesionCreada);
        } catch (Exception e) {
            // Log detallado del error
            throw new RuntimeException("Error al crear sesión: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionResponse> listarSesiones() {
        return listarSesionesUseCase.execute()
            .stream()
            .map(SesionResponse::fromDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionResponse> listarSesionesPorActividad(String actividadId) {
        return listarSesionesUseCase.executeByActividad(actividadId)
            .stream()
            .map(SesionResponse::fromDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SesionResponse obtenerSesionPorId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }

        return listarSesionesUseCase.execute()
            .stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .map(SesionResponse::fromDomain)
            .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada con ID: " + id));
    }

    @Override
    public SesionResponse actualizarSesion(String id, SesionRequest request) {
        // Crear entidad de dominio temporal con los datos a actualizar
        SesionDomainEntity datosActualizados = SesionDomainEntity.create(
            null,
            request.getFechaSesion(),
            request.getTitulo()
        );
        
        return actualizarSesionUseCase.execute(id, datosActualizados)
            .map(SesionResponse::fromDomain)
            .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada con ID: " + id));
    }

    @Override
    public void eliminarSesion(String id) {
        eliminarSesionUseCase.execute(id);
    }
}