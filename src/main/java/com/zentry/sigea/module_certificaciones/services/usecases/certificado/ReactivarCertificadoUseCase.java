package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;

/**
 * Caso de uso para reactivar un certificado revocado
 */
@Component
public class ReactivarCertificadoUseCase {

    private final ICertificadoRepository certificadoRepository;
    private final IEstadoCertificadoRepository estadoCertificadoRepository;

    public ReactivarCertificadoUseCase(
        ICertificadoRepository certificadoRepository,
        IEstadoCertificadoRepository estadoCertificadoRepository
    ) {
        this.certificadoRepository = certificadoRepository;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
    }

    /**
     * Ejecuta la reactivación de un certificado
     */
    public CertificadoDomainEntity execute(String codigoValidacion) {
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
        
        // Obtener estado EMITIDO
        EstadoCertificadoDomainEntity estadoEmitido = estadoCertificadoRepository
            .findByCodigo("EMITIDO")
            .orElseThrow(() -> new IllegalStateException("Estado EMITIDO no encontrado en el sistema"));
        
        // Validaciones de negocio
        validateBusinessRules(certificado);
        
        // Reactivar el certificado usando el método del dominio
        certificado.reactivar(estadoEmitido);
        
        // Guardar y retornar
        return certificadoRepository.save(certificado);
    }
    
    /**
     * Validaciones de reglas de negocio específicas
     */
    private void validateBusinessRules(CertificadoDomainEntity certificado) {
        if (!certificado.estaRevocado()) {
            throw new IllegalStateException(
                "Solo se pueden reactivar certificados que estén revocados"
            );
        }
        
        // TODO: Validar que la inscripción asociada siga siendo válida
        // Esto requeriría consultar el módulo de inscripciones
    }
}