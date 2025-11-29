package com.zentry.sigea.module_certificaciones.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.EstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.ICertificadoService;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.CrearCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.CrearCertificadosMasivosUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.GenerarPdfCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.ObtenerCertificadoPorCodigoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.ReactivarCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.RevocarCertificadoUseCase;


@Service
@Transactional
public class CertificadoServiceImpl implements ICertificadoService {

    private final EstadoCertificadoRepository estadoCertificadoRepository_1;
    
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
    
    public CertificadoServiceImpl(
        CrearCertificadoUseCase crearCertificadoUseCase,
        CrearCertificadosMasivosUseCase crearCertificadosMasivosUseCase,
        ObtenerCertificadoPorCodigoUseCase obtenerCertificadoPorCodigoUseCase,
        RevocarCertificadoUseCase revocarCertificadoUseCase,
        ReactivarCertificadoUseCase reactivarCertificadoUseCase,
        GenerarPdfCertificadoUseCase generarPdfCertificadoUseCase,
        IEstadoCertificadoRepository estadoCertificadoRepository
    , EstadoCertificadoRepository estadoCertificadoRepository_1) {
        this.crearCertificadoUseCase = crearCertificadoUseCase;
        this.crearCertificadosMasivosUseCase = crearCertificadosMasivosUseCase;
        this.obtenerCertificadoPorCodigoUseCase = obtenerCertificadoPorCodigoUseCase;
        this.revocarCertificadoUseCase = revocarCertificadoUseCase;
        this.reactivarCertificadoUseCase = reactivarCertificadoUseCase;
        this.generarPdfCertificadoUseCase = generarPdfCertificadoUseCase;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
        this.estadoCertificadoRepository_1 = estadoCertificadoRepository_1;
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
        
        return obtenerCertificadoPorCodigoUseCase.execute(codigoValidacion)
            .map(this::convertirAResponse);
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
    private CertificadoResponse convertirAResponse(CertificadoDomainEntity certificado) {
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