package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;

/**
 * Caso de uso para obtener un certificado por su código de validación
 */
@Component
public class ObtenerCertificadoPorCodigoUseCase {

    private final ICertificadoRepository certificadoRepository;

    public ObtenerCertificadoPorCodigoUseCase(ICertificadoRepository certificadoRepository) {
        this.certificadoRepository = certificadoRepository;
    }

    /**
     * Ejecuta la búsqueda de certificado por código de validación
     */
    public Optional<CertificadoDomainEntity> execute(String codigoValidacion) {
        // Validaciones de entrada
        if (codigoValidacion == null || codigoValidacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de validación es obligatorio");
        }
        
        // Buscar certificado por código
        return certificadoRepository.findByCodigoValidacion(codigoValidacion.trim());
    }
}