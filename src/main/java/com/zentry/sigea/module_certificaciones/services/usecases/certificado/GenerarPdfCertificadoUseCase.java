package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;

/**
 * Caso de uso para generar el PDF de un certificado
 */
@Component
public class GenerarPdfCertificadoUseCase {

    private final ICertificadoRepository certificadoRepository;

    public GenerarPdfCertificadoUseCase(ICertificadoRepository certificadoRepository) {
        this.certificadoRepository = certificadoRepository;
    }

    /**
     * Ejecuta la generación del PDF para un certificado
     */
    public String execute(String codigoValidacion) {
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
        
        // Validaciones de negocio
        validateBusinessRules(certificado);
        
        // Generar URL del PDF
        String urlPdf = generarUrlPdf(codigoValidacion.trim());
        
        // Establecer la URL en el certificado usando el método del dominio
        certificado.establecerUrlPdf(urlPdf);
        
        // Guardar cambios
        certificadoRepository.save(certificado);
        
        // TODO: Aquí se implementaría la lógica real de generación del PDF
        // Por ejemplo, usar librerías como iText, PDFBox, etc.
        
        return urlPdf;
    }
    
    /**
     * Genera la URL donde estará disponible el PDF
     */
    private String generarUrlPdf(String codigoValidacion) {
        return "/api/certificados/pdf/" + codigoValidacion + ".pdf";
    }
    
    /**
     * Validaciones de reglas de negocio específicas
     */
    private void validateBusinessRules(CertificadoDomainEntity certificado) {
        if (!certificado.esValido()) {
            throw new IllegalStateException(
                "No se puede generar PDF para un certificado inválido o revocado"
            );
        }
        
        if (certificado.estaRevocado()) {
            throw new IllegalStateException(
                "No se puede generar PDF para un certificado revocado"
            );
        }
    }
}