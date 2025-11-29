package com.zentry.sigea.module_certificaciones.services.usecases.validacion;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.TipoValidadorDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.ITipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IValidacionRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;

/**
 * Caso de uso para validar un certificado
 */
@Component
public class ValidarCertificadoUseCase {

    private final IValidacionRepository validacionRepository;
    private final ICertificadoRepository certificadoRepository;
    private final ITipoValidadorRepository tipoValidadorRepository;

    public ValidarCertificadoUseCase(
        IValidacionRepository validacionRepository,
        ICertificadoRepository certificadoRepository,
        ITipoValidadorRepository tipoValidadorRepository
    ) {
        this.validacionRepository = validacionRepository;
        this.certificadoRepository = certificadoRepository;
        this.tipoValidadorRepository = tipoValidadorRepository;
    }

    /**
     * Ejecuta la validación de un certificado
     */
    public ValidacionDomainEntity execute(ValidarCertificadoRequest request) {
        // Validaciones de entrada
        validateInput(request);
        
        // Buscar certificado
        CertificadoDomainEntity certificado = certificadoRepository
            .findByCodigoValidacion(request.getCodigoValidacion())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un certificado con código: " + request.getCodigoValidacion()
            ));
        
        // Buscar tipo de validador
        TipoValidadorDomainEntity tipoValidador = tipoValidadorRepository
            .findByCodigo(request.getTipoValidador())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un tipo de validador con código: " + request.getTipoValidador()
            ));
        
        // Validaciones de negocio
        validateBusinessRules(certificado, tipoValidador, request);
        
        // Verificar si ya existe una validación de este tipo para el certificado
        Optional<ValidacionDomainEntity> validacionExistente = validacionRepository
            .findByCertificadoIdAndTipoValidadorId(
                certificado.getAsistenciaId(), 
                tipoValidador.getCodigo()
            );
        
        ValidacionDomainEntity validacion;
        
        if (validacionExistente.isPresent()) {
            // Actualizar validación existente
            validacion = validacionExistente.get();
            validacion.actualizarResultado(
                determinarResultado(request), 
                request.getDetalle()
            );
        } else {
            // Crear nueva validación
            String resultado = determinarResultado(request);
            if ("APROBADO".equals(resultado)) {
                validacion = ValidacionDomainEntity.crearAprobada(
                    certificado.getAsistenciaId(),
                    tipoValidador.getCodigo(),
                    request.getDetalle()
                );
            } else {
                validacion = ValidacionDomainEntity.crearRechazada(
                    certificado.getAsistenciaId(), 
                    tipoValidador.getCodigo(), 
                    request.getDetalle()
                );
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