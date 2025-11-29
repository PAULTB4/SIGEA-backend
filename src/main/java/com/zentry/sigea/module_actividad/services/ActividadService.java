package com.zentry.sigea.module_actividad.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent;
import com.zentry.sigea.module_notificaciones.events.domain.ActividadCreadaEvent;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.ActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.CrearActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.ActividadResponse;
import com.zentry.sigea.module_actividad.services.interfaces.IActividad;
import com.zentry.sigea.module_actividad.services.usecases.actividad.CrearActividadUseCase;
import com.zentry.sigea.module_actividad.services.usecases.actividad.ActualizarActividadUseCase;
import com.zentry.sigea.module_actividad.services.usecases.actividad.EliminarActividadUseCase;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

/**
 * Servicio de aplicación que orquesta casos de uso de actividades
 * APPLICATION LAYER - Con anotaciones de Spring
 */
@Service
public class ActividadService implements IActividad {

    private final IActividadRespository actividadRespository;
    private final CrearActividadUseCase crearActividadUseCase;
    private final ActualizarActividadUseCase actualizarActividadUseCase;
    private final EliminarActividadUseCase eliminarActividadUseCase;
    private final ApplicationEventPublisher eventPublisher;
    private final UsuarioJPARepository usuarioJPARepository;

    public ActividadService(
        IActividadRespository actividadRepository, 
        CrearActividadUseCase crearActividadUseCase,
        ActualizarActividadUseCase actualizarActividadUseCase,
        EliminarActividadUseCase eliminarActividadUseCase,
        ApplicationEventPublisher eventPublisher,
        UsuarioJPARepository usuarioJPARepository
    ) {
        this.actividadRespository = actividadRepository;
        this.crearActividadUseCase = crearActividadUseCase;
        this.actualizarActividadUseCase = actualizarActividadUseCase;
        this.eliminarActividadUseCase = eliminarActividadUseCase;
        this.eventPublisher = eventPublisher;
        this.usuarioJPARepository = usuarioJPARepository;
    }

    /**
     * Crea una nueva actividad usando el request con IDs
     * Publica evento para notificaciones automáticas a usuarios interesados
     */
    @Override
    public String crearActividad(CrearActividadRequest request) {
        // Crear la actividad
        String actividadId = crearActividadUseCase.execute(request);
        
        // Obtener la actividad recién creada para los detalles
        Optional<ActividadDomainEntity> actividadOpt = actividadRespository.findById(actividadId);
        
        if (actividadOpt.isPresent()) {
            ActividadDomainEntity actividad = actividadOpt.get();
            
            // Obtener TODOS los usuarios registrados para enviarles la notificación
            List<String> usuariosANotificar = usuarioJPARepository.findAll()
                .stream()
                .map(usuario -> usuario.getId().toString())
                .toList();
            
            if (!usuariosANotificar.isEmpty()) {
                // Publicar evento para notificaciones automáticas a TODOS los usuarios
                eventPublisher.publishEvent(new ActividadCreadaEvent(
                    actividadId,
                    actividad.getTitulo(),
                    actividad.getDescripcion(),
                    usuariosANotificar,
                    LocalDateTime.now()
                ));
            }
        }
        
        return actividadId;
    }


    /**
     * Lista actividades con filtros opcionales
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActividadResponse> listarActividades() {
        // Por ahora devolvemos todas, luego implementaremos filtros
        return actividadRespository.findAll().stream()
                .map(ActividadResponse::fromEntity)
                .toList();
    }


    @Override
    @Transactional
    public ActividadResponse actualizarActividad(String id, ActividadRequest request) {
        ActividadDomainEntity actividad = actualizarActividadUseCase.execute(id, request);
        return ActividadResponse.fromEntity(actividad);
    }

    @Override
    public ActividadResponse obtenerActividadPorId(String id) {
        Optional<ActividadDomainEntity> actividad = actividadRespository.findById(id);
        if (actividad.isEmpty()) {
            throw new IllegalArgumentException("Actividad no encontrada con ID: " + id);
        }

        // Usar el factory method de ActividadResponse
        return ActividadResponse.fromEntity(actividad.get());
    }


    @Override
    public List<ActividadResponse> obtenerActividadesPorTipo(String tipoActividadId) {
        return actividadRespository.findByTipoActividadId(tipoActividadId).stream()
                .map(ActividadResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void eliminarActividad(String id) {
        eliminarActividadUseCase.execute(id);
    }
    
    /**
     * Método de ejemplo para enviar comunicaciones a participantes de una actividad
     * Publica un evento que dispara automáticamente notificaciones
     * 
     * EJEMPLO DE USO: Cuando creas una sesión, cambias fechas, o envías un anuncio
     * 
     * @param actividadId ID de la actividad
     * @param usuariosIds Lista de usuarios a notificar
     * @param titulo Título de la comunicación
     * @param mensaje Mensaje de la comunicación
     * @param tipo Tipo de comunicación (NUEVA_SESION, ANUNCIO_ACTIVIDAD, etc.)
     */
    public void enviarComunicacion(
        String actividadId,
        List<String> usuariosIds,
        String titulo,
        String mensaje,
        ComunicacionPublicadaEvent.TipoComunicacion tipo
    ) {
        // Publicar evento para notificaciones automáticas
        eventPublisher.publishEvent(new ComunicacionPublicadaEvent(
            usuariosIds,
            actividadId,
            titulo,
            mensaje,
            tipo,
            LocalDateTime.now()
        ));
    }

}