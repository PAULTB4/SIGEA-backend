package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;

/**
 * Caso de uso para revocar un certificado
 */
@Component
public class RevocarCertificadoUseCase {

    private final ICertificadoRepository certificadoRepository;
    private final IEstadoCertificadoRepository estadoCertificadoRepository;

    public RevocarCertificadoUseCase(
        ICertificadoRepository certificadoRepository,
        IEstadoCertificadoRepository estadoCertificadoRepository
    ) {
        this.certificadoRepository = certificadoRepository;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
    }

    /**
     * Ejecuta la revocación de un certificado
     */
    public CertificadoDomainEntity execute(String codigoValidacion, String motivo) {
        // Validaciones de entrada
        if (codigoValidacion == null || codigoValidacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de validación es obligatorio");
        }
        
        // Buscar certificado
        CertificadoDomainEntity certificado = certificadoRepository
            .findByCodigoValidacion(codigoValidacion.trim())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un certificado con código: " + codigoValidacion
            ));
        
        // Obtener estado REVOCADO
        EstadoCertificadoDomainEntity estadoRevocado = estadoCertificadoRepository
            .findByCodigo("REVOCADO")
            .orElseThrow(() -> new IllegalStateException("Estado REVOCADO no encontrado en el sistema"));
        
        // Validaciones de negocio
        validateBusinessRules(certificado, motivo);
        
        // Revocar el certificado usando el método del dominio
        certificado.revocar(estadoRevocado);
        
        // Guardar y retornar
        return certificadoRepository.save(certificado);
    }
    
    /**
     * Validaciones de reglas de negocio específicas
     */
    private void validateBusinessRules(CertificadoDomainEntity certificado, String motivo) {
        if (certificado.estaRevocado()) {
            throw new IllegalStateException("El certificado ya está revocado");
        }
        
        if (motivo != null && motivo.trim().length() > 500) {
            throw new IllegalArgumentException("El motivo no puede exceder 500 caracteres");
        }
        
        // Validar que el certificado esté en un estado que permita revocación
        if (!certificado.estaEmitido() && !certificado.estaSuspendido()) {
            throw new IllegalStateException(
                "Solo se pueden revocar certificados emitidos o suspendidos"
            );
        }
    }
}