package com.zentry.sigea.module_certificaciones.services.usecases.validacion;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IValidacionRepository;

/**
 * Caso de uso para obtener todas las validaciones de un certificado
 */
@Component
public class ObtenerValidacionesCertificadoUseCase {

    private final IValidacionRepository validacionRepository;
    private final ICertificadoRepository certificadoRepository;

    public ObtenerValidacionesCertificadoUseCase(
        IValidacionRepository validacionRepository,
        ICertificadoRepository certificadoRepository
    ) {
        this.validacionRepository = validacionRepository;
        this.certificadoRepository = certificadoRepository;
    }

    /**
     * Ejecuta la obtención de validaciones para un certificado
     */
    public List<ValidacionDomainEntity> execute(String codigoValidacion) {
        // Validaciones de entrada
        if (codigoValidacion == null || codigoValidacion.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de validación es obligatorio");
        }
        
        // Buscar certificado para validar que existe
        CertificadoDomainEntity certificado = certificadoRepository
            .findByCodigoValidacion(codigoValidacion.trim())
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró un certificado con código: " + codigoValidacion
            ));
        
        // Obtener todas las validaciones del certificado
        return validacionRepository.findByCertificadoId(certificado.getIdCertificado());
    }
}