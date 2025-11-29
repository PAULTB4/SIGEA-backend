package com.zentry.sigea.module_certificaciones.services.interfaces;

import java.util.List;

import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;

/**
 * Interfaz del servicio de validaciones
 * Define los contratos para la gestión de validaciones de certificados
 */
public interface IValidacionService {
    
    /**
     * Valida un certificado con un tipo de validador específico
     */
    ValidacionResponse validarCertificado(ValidarCertificadoRequest request);
    
    /**
     * Obtiene todas las validaciones de un certificado
     */
    List<ValidacionResponse> obtenerValidacionesCertificado(String codigoValidacion);
    
    /**
     * Obtiene validaciones por tipo de validador
     */
    List<ValidacionResponse> obtenerValidacionesPorTipo(String tipoValidador);
    
    /**
     * Obtiene validaciones por resultado
     */
    List<ValidacionResponse> obtenerValidacionesPorResultado(String resultado);
}