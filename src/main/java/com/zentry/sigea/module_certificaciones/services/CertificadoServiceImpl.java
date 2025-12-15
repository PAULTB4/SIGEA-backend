        // ...existing code...
    // ...existing code...
package com.zentry.sigea.module_certificaciones.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.zentry.sigea.module_certificaciones.core.repositories.IValidacionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.EstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.TipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.ICertificadoService;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.CrearCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.CrearCertificadosMasivosUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.GenerarPdfCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.ObtenerCertificadoPorCodigoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.ReactivarCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.RevocarCertificadoUseCase;

import com.zentry.sigea.module_certificaciones.infrastructure.storage.StorageService;
import org.springframework.web.multipart.MultipartFile;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.ITipoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;


@Service
@Transactional
public class CertificadoServiceImpl implements ICertificadoService {
            private final org.springframework.context.ApplicationEventPublisher eventPublisher;
        /**
         * Publica un evento de certificado EMITIDO para que el listener de notificaciones lo procese
         */
        private void publicarEventoCertificadoEmitido(CertificadoDomainEntity certificado) {
            try {
                String asistenciaId = certificado.getAsistenciaId();
                if (asistenciaId == null) {
                    log.warn("El certificado no tiene asistenciaId, no se puede notificar");
                    return;
                }
                inscripcionRepository.findById(asistenciaId).ifPresent(inscripcion -> {
                    com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent evento =
                        new com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent(
                            inscripcion.getUsuarioId(),
                            certificado.getIdCertificado(),
                            inscripcion.getActividadId(),
                            null, // actividadTitulo (opcional)
                            certificado.getCodigoValidacion(),
                            certificado.getFechaEmision(),
                            com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent.EstadoCertificado.EMITIDO,
                            certificado.getUrlPdf(),
                            asistenciaId,
                            certificado.getCreatedAt() != null ? certificado.getCreatedAt() : java.time.LocalDateTime.now()
                        );
                    eventPublisher.publishEvent(evento);
                    log.info("Evento de certificado EMITIDO publicado para usuario {} y certificado {}", inscripcion.getUsuarioId(), certificado.getIdCertificado());
                });
            } catch (Exception ex) {
                log.error("Error publicando evento de certificado EMITIDO: {}", ex.getMessage());
            }
        }
    private final TipoValidadorRepository tipoValidadorRepository;
    private final IValidacionRepository validacionRepository;
    private final ICertificadoRepository certificadoRepository;
    private final NotificacionService notificacionService;
    private final IInscripcionRepository inscripcionRepository;
    private final IEstadoNotificacionRepository estadoNotificacionRepository;
    private final ITipoNotificacionRepository tipoNotificacionRepository;
    @Override
    public CertificadoResponse crearCertificadoConArchivo(CrearCertificadoRequest request, MultipartFile file) {
        log.info("Creando certificado a partir de archivo subido para asistencia: {}", request.getAsistenciaId());
        try {
            // 1. Subir el archivo a storage y obtener la URL pública
            String pathDestino = (request.getNombreArchivo() != null && !request.getNombreArchivo().isEmpty())
                ? request.getNombreArchivo()
                : file.getOriginalFilename();
            String urlPublica = storageService.uploadFile(file, pathDestino);

            // 2. Crear la entidad de dominio y guardar el certificado
            CertificadoDomainEntity certificado = crearCertificadoUseCase.execute(request);
            certificado.setUrlPdf(urlPublica);

            // Guardar cambios en el repositorio para persistir la URL
            certificadoRepository.save(certificado);

            log.info("Certificado creado y archivo subido exitosamente. URL: {}", urlPublica);
            return convertirAResponse(certificado);
        } catch (Exception e) {
            log.error("Error al crear certificado con archivo subido: {}", e.getMessage());
            throw new RuntimeException("Error al crear certificado con archivo subido: " + e.getMessage(), e);
        }
    }

    private final EstadoCertificadoRepository estadoCertificadoRepository_1;
    private final StorageService storageService;
    
    private static final Logger log = LoggerFactory.getLogger(CertificadoServiceImpl.class);
    
    // Use Cases
    private final CrearCertificadoUseCase crearCertificadoUseCase;
    private final CrearCertificadosMasivosUseCase crearCertificadosMasivosUseCase;
    private final ObtenerCertificadoPorCodigoUseCase obtenerCertificadoPorCodigoUseCase;
    private final RevocarCertificadoUseCase revocarCertificadoUseCase;
    private final ReactivarCertificadoUseCase reactivarCertificadoUseCase;
    private final GenerarPdfCertificadoUseCase generarPdfCertificadoUseCase;
    
    // Repository para consultas adicionales
    private final IEstadoCertificadoRepository estadoCertificadoRepository;
    
    @Autowired
    public CertificadoServiceImpl(
        CrearCertificadoUseCase crearCertificadoUseCase,
        CrearCertificadosMasivosUseCase crearCertificadosMasivosUseCase,
        ObtenerCertificadoPorCodigoUseCase obtenerCertificadoPorCodigoUseCase,
        RevocarCertificadoUseCase revocarCertificadoUseCase,
        ReactivarCertificadoUseCase reactivarCertificadoUseCase,
        GenerarPdfCertificadoUseCase generarPdfCertificadoUseCase,
        IEstadoCertificadoRepository estadoCertificadoRepository,
        EstadoCertificadoRepository estadoCertificadoRepository_1,
        StorageService storageService,
        IValidacionRepository validacionRepository,
        ICertificadoRepository certificadoRepository,
        TipoValidadorRepository tipoValidadorRepository,
        NotificacionService notificacionService,
        IInscripcionRepository inscripcionRepository,
        IEstadoNotificacionRepository estadoNotificacionRepository,
        ITipoNotificacionRepository tipoNotificacionRepository,
        org.springframework.context.ApplicationEventPublisher eventPublisher
    ) {
        this.crearCertificadoUseCase = crearCertificadoUseCase;
        this.crearCertificadosMasivosUseCase = crearCertificadosMasivosUseCase;
        this.obtenerCertificadoPorCodigoUseCase = obtenerCertificadoPorCodigoUseCase;
        this.revocarCertificadoUseCase = revocarCertificadoUseCase;
        this.reactivarCertificadoUseCase = reactivarCertificadoUseCase;
        this.generarPdfCertificadoUseCase = generarPdfCertificadoUseCase;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
        this.estadoCertificadoRepository_1 = estadoCertificadoRepository_1;
        this.storageService = storageService;
        this.validacionRepository = validacionRepository;
        this.certificadoRepository = certificadoRepository;
        this.tipoValidadorRepository = tipoValidadorRepository;
        this.notificacionService = notificacionService;
        this.inscripcionRepository = inscripcionRepository;
        this.estadoNotificacionRepository = estadoNotificacionRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String subirCertificado(MultipartFile file, String pathDestino) throws Exception {
        if (storageService == null) {
            throw new IllegalStateException("StorageService no está configurado");
        }
        return storageService.uploadFile(file, pathDestino);
    }
    
    @Override
    public CertificadoResponse crearCertificado(CrearCertificadoRequest request) {
        log.info("Creando certificado para asistencia: {}", request.getAsistenciaId());
        
        try {
            CertificadoDomainEntity certificadoCreado = crearCertificadoUseCase.execute(request);
            
            log.info("Certificado creado exitosamente con ID: {} y código: {}", 
                    certificadoCreado.getIdCertificado(), certificadoCreado.getCodigoValidacion());
            
            return convertirAResponse(certificadoCreado);
            
        } catch (Exception e) {
            log.error("Error al crear certificado para asistencia {}: {}", 
                        request.getAsistenciaId(), e.getMessage());
            throw e;
        }
    }
    
    @Override
    public Map<String , Boolean> crearCertificadosMasivos(List<String> listActividadIds){
        try{
            return crearCertificadosMasivosUseCase.execute(listActividadIds);
        } catch (Exception e) {
            log.error("Error al crear certificado para las asistenciacias");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoResponse> buscarCertificadoPorCodigo(String codigoValidacion) {
        log.debug("Buscando certificado por código: {}", codigoValidacion);
        Optional<CertificadoDomainEntity> certificadoOpt = obtenerCertificadoPorCodigoUseCase.execute(codigoValidacion);
        if (certificadoOpt.isEmpty()) {
            return Optional.empty();
        }
        CertificadoDomainEntity certificado = certificadoOpt.get();
        // Verificar si el certificado tiene al menos una validación exitosa (APROBADO)
        // Buscar el UUID del tipo validador 'ADMIN'
        UUID tipoValidadorId = null;
        boolean validado = false;
        try {
            var tipoValidadorOpt = tipoValidadorRepository.findByCodigo("ADMIN");
            if (tipoValidadorOpt.isPresent()) {
                tipoValidadorId = tipoValidadorOpt.get().getIdTipoValidador();
                validado = validacionRepository
                    .findByCertificadoIdAndTipoValidadorId(certificado.getIdCertificado(), tipoValidadorId)
                    .map(val -> "APROBADO".equalsIgnoreCase(val.getResultado()))
                    .orElse(false);
            }
        } catch (Exception e) {
            log.error("Error al buscar tipo validador ADMIN: {}", e.getMessage());
        }
        if (!validado) {
            // No permitir descarga si no está validado
            log.warn("Intento de acceso a certificado no validado: {}", codigoValidacion);
            return Optional.empty();
        }
        // Si el certificado fue validado, publicar evento EMITIDO para notificación automática
        publicarEventoCertificadoEmitido(certificado);
        return Optional.of(convertirAResponse(certificado));
    }

    /**
     * Envía la notificación de certificado EMITIDO a los usuarios inscritos
     */
    private void enviarNotificacionCertificadoEmitido(CertificadoDomainEntity certificado) {
        try {
            String asistenciaId = certificado.getAsistenciaId();
            if (asistenciaId == null) {
                log.warn("El certificado no tiene asistenciaId, no se puede notificar");
                return;
            }
            inscripcionRepository.findById(asistenciaId).ifPresent(inscripcion -> {
                String usuarioId = inscripcion.getUsuarioId();
                String actividadId = inscripcion.getActividadId();
                String estadoCodigo = "EMITIDO";
                String tipoCodigo = "CERTIFICADO";
                String estadoId = estadoNotificacionRepository.findByCodigo(estadoCodigo)
                    .map(e -> e.getId()).orElse(null);
                String tipoId = tipoNotificacionRepository.findByCodigo(tipoCodigo)
                    .map(t -> t.getId()).orElse(null);
                if (estadoId == null || tipoId == null) {
                    log.warn("No se encontró estado o tipo de notificación para CERTIFICADO EMITIDO");
                    return;
                }
                String mensaje = "¡Tu certificado ya está disponible para descargar!\n" +
                        (certificado.getUrlPdf() != null ? "Descárgalo aquí: " + certificado.getUrlPdf() : "");
                CrearNotificacionRequest notiRequest = new CrearNotificacionRequest(
                    usuarioId,
                    actividadId,
                    tipoId,
                    mensaje,
                    estadoId,
                    "SISTEMA"
                );
                notificacionService.crearNotificacion(notiRequest);
            });
        } catch (Exception ex) {
            log.error("Error enviando notificación de certificado EMITIDO: {}", ex.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CertificadoResponse> buscarCertificadoPorInscripcion(String inscripcionId) {
        log.debug("Buscando certificado por inscripción: {}", inscripcionId);
        
        // TODO: Implementar caso de uso específico para buscar por inscripción
        // Por ahora implementamos lógica directa
        
        return Optional.empty(); // Placeholder
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CertificadoResponse> obtenerTodosCertificados() {
        log.debug("Obteniendo todos los certificados");
        
        // TODO: Crear caso de uso para obtener todos los certificados
        // Por ahora implementamos lógica directa
        
        return List.of(); // Placeholder
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CertificadoResponse> obtenerCertificadosPorEstado(String codigoEstado) {
        log.debug("Buscando certificados por estado: {}", codigoEstado);
        
        // TODO: Crear caso de uso específico para buscar por estado
        // Por ahora implementamos lógica directa
        
        return List.of(); // Placeholder
    }
    
    @Override
    public CertificadoResponse revocarCertificado(String codigoValidacion, String motivo) {
        log.info("Revocando certificado: {} por motivo: {}", codigoValidacion, motivo);
        
        try {
            CertificadoDomainEntity certificadoRevocado = revocarCertificadoUseCase.execute(codigoValidacion, motivo);
            
            log.info("Certificado revocado exitosamente: {}", codigoValidacion);
            
            return convertirAResponse(certificadoRevocado);
            
        } catch (Exception e) {
            log.error("Error al revocar certificado {}: {}", codigoValidacion, e.getMessage());
            throw e;
        }
    }
    
    @Override
    public CertificadoResponse reactivarCertificado(String codigoValidacion) {
        log.info("Reactivando certificado: {}", codigoValidacion);
        
        try {
            CertificadoDomainEntity certificadoReactivado = reactivarCertificadoUseCase.execute(codigoValidacion);
            
            log.info("Certificado reactivado exitosamente: {}", codigoValidacion);
            
            return convertirAResponse(certificadoReactivado);
            
        } catch (Exception e) {
            log.error("Error al reactivar certificado {}: {}", codigoValidacion, e.getMessage());
            throw e;
        }
    }
    
    @Override
    public String generarPdfCertificado(String codigoValidacion) {
        log.info("Generando PDF para certificado: {}", codigoValidacion);
        
        try {
            String urlPdf = generarPdfCertificadoUseCase.execute(codigoValidacion);
            
            log.info("PDF generado exitosamente para certificado: {} en: {}", codigoValidacion, urlPdf);
            
            return urlPdf;
            
        } catch (Exception e) {
            log.error("Error al generar PDF para certificado {}: {}", codigoValidacion, e.getMessage());
            throw e;
        }
    }
    
    // Métodos auxiliares de conversión
    
    /**
     * Convierte una entidad de dominio a DTO de respuesta
     */
    public CertificadoResponse convertirAResponse(CertificadoDomainEntity certificado) {
        CertificadoResponse response = new CertificadoResponse();
        response.setIdCertificado(certificado.getIdCertificado());
        
        // Convertir String a Long para el DTO (manteniendo compatibilidad)
        try {
            response.setAsistenciaId(certificado.getAsistenciaId());
        } catch (NumberFormatException e) {
            log.warn("Error al convertir asistenciaId {} a Long", certificado.getAsistenciaId());
            response.setAsistenciaId(null);
        }
        
        response.setCodigoValidacion(certificado.getCodigoValidacion());
        response.setFechaEmision(certificado.getFechaEmision());
        response.setEstado(certificado.getEstado() != null ? certificado.getEstado().getCodigo() : null);
        response.setUrlPdf(certificado.getUrlPdf());
        response.setCreatedAt(certificado.getCreatedAt());
        response.setUpdatedAt(certificado.getUpdatedAt());
        
        // TODO: Agregar datos del usuario y actividad consultando otros módulos
        // Esto requeriría servicios o adaptadores para comunicación entre módulos
        
        return response;
    }

}