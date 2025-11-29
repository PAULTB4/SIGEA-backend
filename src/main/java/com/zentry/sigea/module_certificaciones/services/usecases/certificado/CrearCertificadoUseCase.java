package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent.EstadoCertificado;

/**
 * Caso de uso para crear un nuevo certificado
 */
@Component
public class CrearCertificadoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CrearCertificadoUseCase.class);

    private final ICertificadoRepository certificadoRepository;
    private final IEstadoCertificadoRepository estadoCertificadoRepository;
    private final IAsistenciaRepository asistenciaRepository;
    private final IInscripcionRepository inscripcionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CrearCertificadoUseCase(
        ICertificadoRepository certificadoRepository,
        IEstadoCertificadoRepository estadoCertificadoRepository,
        IAsistenciaRepository asistenciaRepository,
        IInscripcionRepository inscripcionRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.certificadoRepository = certificadoRepository;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Ejecuta la creación de certificado
     */
    public CertificadoDomainEntity execute(CrearCertificadoRequest request) {
        // Validar que no exista ya un certificado para esta inscripción
        String asistenciaId = request.getAsistenciaId();
        if (certificadoRepository.existsByAsistenciaId(asistenciaId)) {
            throw new IllegalArgumentException(
                "Ya existe un certificado para esta asistencia"
            );
        }
        
        // Obtener estado EMITIDO por defecto
        EstadoCertificadoDomainEntity estadoEmitido = estadoCertificadoRepository
            .findByCodigo("EMITIDO")
            .orElseThrow(() -> new IllegalStateException("Estado EMITIDO no encontrado en el sistema"));
        
        // Generar código de validación único
        String codigoValidacion = generarCodigoValidacion();
        
        // Asegurar que el código sea único
        while (certificadoRepository.existsByCodigoValidacion(codigoValidacion)) {
            codigoValidacion = generarCodigoValidacion();
        }
        
        // Validaciones de negocio específicas del caso de uso
        validateBusinessRules(request);
        
        // Crear la entidad usando el factory method del dominio
        CertificadoDomainEntity nuevoCertificado = CertificadoDomainEntity.create(
            asistenciaId,
            codigoValidacion,
            estadoEmitido
        );
        
        // Guardar usando el repositorio
        CertificadoDomainEntity certificadoGuardado = certificadoRepository.save(nuevoCertificado);
        
        // Publicar evento de notificación
        try {
            AsistenciaDomainEntity asistencia = asistenciaRepository.findById(asistenciaId.toString())
                .orElseThrow(() -> new IllegalArgumentException("Asistencia no encontrada: " + asistenciaId));
            
            InscripcionDomainEntity inscripcion = inscripcionRepository.findById(asistencia.getInscripcionId())
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada: " + asistencia.getInscripcionId()));
            
            logger.info("📧 Publicando evento CertificadoGeneradoEvent para certificado: {}", certificadoGuardado.getIdCertificado());
            
            eventPublisher.publishEvent(new CertificadoGeneradoEvent(
                inscripcion.getUsuarioId(),                    // ID del usuario desde inscripción
                certificadoGuardado.getIdCertificado(),        // ID del certificado
                inscripcion.getActividadId(),                  // ID de la actividad desde inscripción
                "Actividad Completada",                        // Título (se puede obtener del servicio de actividades)
                certificadoGuardado.getCodigoValidacion(),     // Código de validación
                certificadoGuardado.getFechaEmision(),         // Fecha de emisión
                EstadoCertificado.EMITIDO,                     // Estado del certificado
                certificadoGuardado.getUrlPdf(),               // URL del PDF (puede ser null)
                asistenciaId.toString(),                        // ID de asistencia
                LocalDateTime.now()                             // Fecha de generación
            ));
            
            logger.info("✅ Evento CertificadoGeneradoEvent publicado exitosamente");
            
        } catch (Exception e) {
            logger.error("❌ Error publicando evento de notificación para certificado {}: {}", 
                certificadoGuardado.getIdCertificado(), e.getMessage(), e);
            // No lanzamos la excepción para que el certificado se cree aunque falle la notificación
        }
        
        return certificadoGuardado;
    }

    /**
     * Genera un código de validación único
     */
    private String generarCodigoValidacion() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Validaciones de reglas de negocio específicas
     */
    private void validateBusinessRules(CrearCertificadoRequest request) {
        if (request.getAsistenciaId() == null) {
            throw new IllegalArgumentException("La asistencia ID es obligatoria");
        }
        
        // TODO: Validar que la asistencia exista y esté completada
        // Esto requeriría una consulta al módulo de inscripciones
        // Por ahora solo validamos el formato básico
    }
}