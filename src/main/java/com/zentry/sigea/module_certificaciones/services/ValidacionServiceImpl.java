package com.zentry.sigea.module_certificaciones.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.IValidacionService;
import com.zentry.sigea.module_certificaciones.services.usecases.validacion.ObtenerValidacionesCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.validacion.ValidarCertificadoUseCase;

@Service
@Transactional
public class ValidacionServiceImpl implements IValidacionService {
    
    private static final Logger log = LoggerFactory.getLogger(ValidacionServiceImpl.class);
    
    // Use Cases
    private final ValidarCertificadoUseCase validarCertificadoUseCase;
    private final ObtenerValidacionesCertificadoUseCase obtenerValidacionesCertificadoUseCase;
    
    public ValidacionServiceImpl(
        ValidarCertificadoUseCase validarCertificadoUseCase,
        ObtenerValidacionesCertificadoUseCase obtenerValidacionesCertificadoUseCase
    ) {
        this.validarCertificadoUseCase = validarCertificadoUseCase;
        this.obtenerValidacionesCertificadoUseCase = obtenerValidacionesCertificadoUseCase;
    }
    
    @Override
    public ValidacionResponse validarCertificado(ValidarCertificadoRequest request) {
        log.info("Validando certificado: {} con tipo: {}", 
                request.getCodigoValidacion(), request.getTipoValidador());
        
        try {
            ValidacionDomainEntity validacionCreada = validarCertificadoUseCase.execute(request);
            
            log.info("Validación creada exitosamente con ID: {}", validacionCreada.getTipoValidador());
            
            return convertirAValidacionResponse(validacionCreada);
            
        } catch (Exception e) {
            log.error("Error al validar certificado {}: {}", 
                     request.getCodigoValidacion(), e.getMessage());
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ValidacionResponse> obtenerValidacionesCertificado(String codigoValidacion) {
        log.debug("Obteniendo validaciones para certificado: {}", codigoValidacion);
        
        List<ValidacionDomainEntity> validaciones = obtenerValidacionesCertificadoUseCase.execute(codigoValidacion);
        
        return validaciones.stream()
            .map(this::convertirAValidacionResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ValidacionResponse> obtenerValidacionesPorTipo(String tipoValidador) {
        log.debug("Obteniendo validaciones por tipo: {}", tipoValidador);
        
        // TODO: Crear caso de uso específico para buscar por tipo de validador
        // Por ahora implementamos lógica directa
        
        return List.of(); // Placeholder
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ValidacionResponse> obtenerValidacionesPorResultado(String resultado) {
        log.debug("Obteniendo validaciones por resultado: {}", resultado);
        
        // TODO: Crear caso de uso específico para buscar por resultado
        // Por ahora implementamos lógica directa
        
        return List.of(); // Placeholder
    }
    
    // Métodos auxiliares de conversión
    
    /**
     * Convierte una entidad de dominio de validación a DTO de respuesta
     */
    private ValidacionResponse convertirAValidacionResponse(ValidacionDomainEntity validacion) {
        ValidacionResponse response = new ValidacionResponse();
        // Generar un ID temporal basado en certificado y tipo de validador
        // En una implementación real, esto vendría de la base de datos
        if (validacion.getCertificado() != null && validacion.getTipoValidador() != null) {
            response.setIdValidacion(validacion.getCertificado() + "_" + validacion.getTipoValidador());
        }
        
        response.setCodigoValidacion(validacion.getCertificado());
        response.setTipoValidador(validacion.getTipoValidador());
        response.setFechaValidacion(validacion.getFechaValidacion());
        response.setResultado(validacion.getResultado());
        response.setDetalle(validacion.getDetalle());
        
        // TODO: Establecer el certificado completo si es necesario
        // response.setCertificado(convertirAResponse(validacion.getCertificado()));
        
        return response;
    }
}