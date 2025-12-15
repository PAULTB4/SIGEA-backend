package com.zentry.sigea.module_certificaciones.services.usecases.validacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.TipoValidadorDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.ITipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.TipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.IValidacionRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;

/**
 * Caso de uso para validar un certificado
 */
@Component
public class ValidarCertificadoUseCase {
    private static final Logger logger = LoggerFactory.getLogger(ValidarCertificadoUseCase.class);

    private final org.springframework.context.ApplicationEventPublisher eventPublisher;
    private final IInscripcionRepository inscripcionRepository;
    private final IAsistenciaRepository asistenciaRepository;

    private final IValidacionRepository validacionRepository;
    private final ICertificadoRepository certificadoRepository;
    private final ITipoValidadorRepository tipoValidadorRepository;
    private final TipoValidadorRepository tipoValidadorJpaRepository;
    private final com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository estadoCertificadoRepository;

    public ValidarCertificadoUseCase(
        IValidacionRepository validacionRepository,
        ICertificadoRepository certificadoRepository,
        ITipoValidadorRepository tipoValidadorRepository,
        com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository estadoCertificadoRepository,
        TipoValidadorRepository tipoValidadorJpaRepository,
        org.springframework.context.ApplicationEventPublisher eventPublisher,
        IInscripcionRepository inscripcionRepository,
        IAsistenciaRepository asistenciaRepository
    ) {
        this.validacionRepository = validacionRepository;
        this.certificadoRepository = certificadoRepository;
        this.tipoValidadorRepository = tipoValidadorRepository;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
        this.tipoValidadorJpaRepository = tipoValidadorJpaRepository;
        this.eventPublisher = eventPublisher;
        this.inscripcionRepository = inscripcionRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

    /**
     * Ejecuta la validación de un certificado
     */
    public ValidacionDomainEntity execute(ValidarCertificadoRequest request) {
            logger.info("[VALIDACIÓN] Iniciando validación de certificado: {}", request.getCodigoValidacion());
        // Validaciones de entrada
        validateInput(request);

        // Buscar certificado
        CertificadoDomainEntity certificado = certificadoRepository
            .findByCodigoValidacion(request.getCodigoValidacion())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un certificado con código: " + request.getCodigoValidacion()
            ));

        // Buscar tipo de validador (dominio)
        TipoValidadorDomainEntity tipoValidador = tipoValidadorRepository
            .findByCodigo(request.getTipoValidador())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un tipo de validador con código: " + request.getTipoValidador()
            ));

        // Buscar tipo de validador (infraestructura/JPA) para obtener el UUID
        TipoValidadorEntity tipoValidadorEntity = tipoValidadorJpaRepository
            .findByCodigo(request.getTipoValidador())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró el tipo de validador en la base de datos: " + request.getTipoValidador()
            ));

        // Validaciones de negocio
        validateBusinessRules(certificado, tipoValidador, request);

        // Verificar si ya existe una validación de este tipo para el certificado usando el UUID
        Optional<ValidacionDomainEntity> validacionExistente = validacionRepository
            .findByCertificadoIdAndTipoValidadorId(
                certificado.getIdCertificado(),
                tipoValidadorEntity.getIdTipoValidador()
            );

        ValidacionDomainEntity validacion;
        String resultado = determinarResultado(request);

        if (validacionExistente.isPresent()) {
            // Actualizar validación existente
            validacion = validacionExistente.get();
            validacion.actualizarResultado(
                resultado,
                request.getDetalle()
            );
        } else {
            // Crear nueva validación
            if ("APROBADO".equals(resultado)) {
                            logger.info("[VALIDACIÓN] Certificado validado correctamente. Publicando evento de notificación EMITIDO...");
                validacion = ValidacionDomainEntity.crearAprobada(
                    certificado.getIdCertificado(),
                    tipoValidador.getCodigo(),
                    request.getDetalle()
                );
            } else {
                validacion = ValidacionDomainEntity.crearRechazada(
                    certificado.getIdCertificado(),
                    tipoValidador.getCodigo(),
                    request.getDetalle()
                );
            }
        }

        // Si la validación fue aprobada, cambiar estado del certificado a EMITIDO
        if ("APROBADO".equals(resultado)) {
            // Buscar estado EMITIDO
            EstadoCertificadoDomainEntity estadoEmitido = estadoCertificadoRepository.findByCodigo("EMITIDO")
                .orElse(EstadoCertificadoDomainEntity.emitido());
            certificado.cambiarEstado(estadoEmitido);
            certificadoRepository.save(certificado);

            // Notificación automática (EMITIDO) por asistencias reales de la sesión
            try {
                String asistenciaId = certificado.getAsistenciaId();
                if (asistenciaId != null) {
                    // Buscar la asistencia original para obtener la sesión
                    var asistenciaOpt = asistenciaRepository.findById(asistenciaId);
                    if (asistenciaOpt.isPresent()) {
                        var asistencia = asistenciaOpt.get();
                        String sesionId = asistencia.getSesionId();
                        // Buscar todas las asistencias de esa sesión
                        var asistenciasSesion = asistenciaRepository.findBySesionId(sesionId);
                        for (var asistenciaSesion : asistenciasSesion) {
                            // Para cada asistencia, obtener la inscripción y el usuario
                            var inscripcionOpt = inscripcionRepository.findById(asistenciaSesion.getInscripcionId());
                            if (inscripcionOpt.isPresent()) {
                                var inscripcion = inscripcionOpt.get();
                                String usuarioId = inscripcion.getUsuarioId();
                                String actividadId = inscripcion.getActividadId();
                                logger.info("[EVENTO] Publicando notificación EMITIDO para asistenciaId={}, usuarioId={}, actividadId={}, certificadoId={}", asistenciaSesion.getId(), usuarioId, actividadId, certificado.getIdCertificado());
                                eventPublisher.publishEvent(
                                    new com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent(
                                        usuarioId,
                                        certificado.getIdCertificado(),
                                        actividadId,
                                        null, // actividadTitulo (opcional)
                                        certificado.getCodigoValidacion(),
                                        certificado.getFechaEmision(),
                                        com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent.EstadoCertificado.EMITIDO,
                                        certificado.getUrlPdf(),
                                        asistenciaSesion.getId(),
                                        certificado.getCreatedAt() != null ? certificado.getCreatedAt() : java.time.LocalDateTime.now()
                                    )
                                );
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("[ERROR] Error publicando evento de notificación EMITIDO: {}", e.getMessage(), e);
            }
        }

        // Guardar y retornar
        return validacionRepository.save(validacion);
    }
    
    /**
     * Valida los datos de entrada
     */
    private void validateInput(ValidarCertificadoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de validación es obligatoria");
        }
        
        if (request.getCodigoValidacion() == null || request.getCodigoValidacion().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de validación es obligatorio");
        }
        
        if (request.getTipoValidador() == null || request.getTipoValidador().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de validador es obligatorio");
        }
    }
    
    /**
     * Validaciones de reglas de negocio específicas
     */
    private void validateBusinessRules(CertificadoDomainEntity certificado, 
                                     TipoValidadorDomainEntity tipoValidador, 
                                     ValidarCertificadoRequest request) {
        if (!certificado.esValido()) {
            throw new IllegalStateException("No se puede validar un certificado inválido");
        }
        
        if (certificado.estaRevocado()) {
            throw new IllegalStateException("No se puede validar un certificado revocado");
        }
        
        if (!tipoValidador.esValido()) {
            throw new IllegalStateException("El tipo de validador no es válido");
        }
        
        // Si el tipo de validador requiere conexión y estamos offline, lanzar excepción
        if (tipoValidador.requiereConexion() && !isOnline()) {
            throw new IllegalStateException(
                "Este tipo de validación requiere conexión a internet"
            );
        }
    }
    
    /**
     * Determina el resultado de la validación basado en la request
     * Por ahora, asume que todas las validaciones son aprobadas
     * En una implementación real, aquí iría la lógica específica de validación
     */
    private String determinarResultado(ValidarCertificadoRequest request) {
        // TODO: Implementar lógica real de validación según el tipo
        // Por ejemplo, para QR verificar el código, para HASH verificar la integridad, etc.
        return "APROBADO";
    }
    
    /**
     * Verifica si hay conexión a internet (simulado)
     * En una implementación real, esto verificaría la conectividad
     */
    private boolean isOnline() {
        // TODO: Implementar verificación real de conectividad
        return true;
    }
}