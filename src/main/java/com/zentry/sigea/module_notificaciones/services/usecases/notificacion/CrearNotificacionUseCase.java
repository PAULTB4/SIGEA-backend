package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_notificaciones.core.entities.CanalNotificacion;
import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.ITipoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;

/**
 * Caso de uso: Crear una nueva notificación
 * Responsabilidad: validar datos y orquestar la creación de notificación
 */
@Component
public class CrearNotificacionUseCase {

    private final INotificacionRepository notificacionRepository;
    private final ITipoNotificacionRepository tipoNotificacionRepository;
    private final IEstadoNotificacionRepository estadoNotificacionRepository;

    public CrearNotificacionUseCase(
        INotificacionRepository notificacionRepository,
        ITipoNotificacionRepository tipoNotificacionRepository,
        IEstadoNotificacionRepository estadoNotificacionRepository
    ) {
        this.notificacionRepository = notificacionRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.estadoNotificacionRepository = estadoNotificacionRepository;
    }

    public NotificacionDomainEntity execute(CrearNotificacionRequest request) {
        // Validaciones básicas
        validateBasicFields(request);
        
        // Obtener tipo de notificación (buscar por código si no es UUID)
        TipoNotificacionDomainEntity tipoNotificacion;
        String tipoId = request.getTipoNotificacionId();
        
        // Intentar buscar por código si no parece un UUID
        if (tipoId != null && !tipoId.contains("-")) {
            tipoNotificacion = tipoNotificacionRepository.findByCodigo(tipoId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró el tipo de notificación con código: " + tipoId
                ));
        } else {
            tipoNotificacion = tipoNotificacionRepository.findById(tipoId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró el tipo de notificación con ID: " + tipoId
                ));
        }
        
        // Obtener estado de notificación (usar PENDIENTE si no se especifica)
        EstadoNotificacionDomainEntity estadoNotificacion;
        if (request.getEstadoNotificacionId() == null || request.getEstadoNotificacionId().trim().isEmpty()) {
            // Buscar estado PENDIENTE por código
            estadoNotificacion = estadoNotificacionRepository.findByCodigo("PENDIENTE")
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró el estado PENDIENTE. Verifica que exista en la base de datos."
                ));
        } else {
            estadoNotificacion = estadoNotificacionRepository.findById(request.getEstadoNotificacionId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "No se encontró el estado de notificación con ID: " + request.getEstadoNotificacionId()
                ));
        }
        
        // Validar y obtener canal
        CanalNotificacion canal = CanalNotificacion.fromString(
            request.getCanal() != null ? request.getCanal() : "SISTEMA"
        );
        
        // Crear la notificación usando el factory method del dominio
        NotificacionDomainEntity nuevaNotificacion = NotificacionDomainEntity.create(
            request.getUsuarioId(),
            request.getActividadId(),
            tipoNotificacion,
            request.getMensaje(),
            estadoNotificacion,
            canal
        );
        
        // Guardar y retornar la entidad creada
        boolean guardado = notificacionRepository.save(nuevaNotificacion);
        
        if (!guardado) {
            throw new RuntimeException("Error al guardar la notificación en la base de datos");
        }
        
        return nuevaNotificacion;
    }

    private void validateBasicFields(CrearNotificacionRequest request) {
        if (request.getUsuarioId() == null || request.getUsuarioId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        
        if (request.getTipoNotificacionId() == null || request.getTipoNotificacionId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del tipo de notificación es obligatorio");
        }
        
        if (request.getMensaje() == null || request.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje es obligatorio");
        }
        
        // El estadoNotificacionId puede ser null, se asignará "PENDIENTE" por defecto
        
        // Validar canal si se proporciona
        if (request.getCanal() != null && !request.getCanal().trim().isEmpty()) {
            try {
                CanalNotificacion.fromString(request.getCanal());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Canal inválido. Los canales válidos son: SISTEMA, CORREO, WHATSAPP");
            }
        }
    }
}
