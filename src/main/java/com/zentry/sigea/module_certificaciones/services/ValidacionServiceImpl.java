package com.zentry.sigea.module_certificaciones.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.IValidacionService;
import com.zentry.sigea.module_certificaciones.services.usecases.validacion.ObtenerValidacionesCertificadoUseCase;
import com.zentry.sigea.module_certificaciones.services.usecases.validacion.ValidarCertificadoUseCase;

@Service
@Transactional
public class ValidacionServiceImpl implements IValidacionService {

    private static final Logger log = LoggerFactory.getLogger(ValidacionServiceImpl.class);

    private final ValidarCertificadoUseCase validarCertificadoUseCase;
    private final ObtenerValidacionesCertificadoUseCase obtenerValidacionesCertificadoUseCase;
    private final ICertificadoRepository certificadoRepository;

    public ValidacionServiceImpl(
        ValidarCertificadoUseCase validarCertificadoUseCase,
        ObtenerValidacionesCertificadoUseCase obtenerValidacionesCertificadoUseCase,
        ICertificadoRepository certificadoRepository
    ) {
        this.validarCertificadoUseCase = validarCertificadoUseCase;
        this.obtenerValidacionesCertificadoUseCase = obtenerValidacionesCertificadoUseCase;
        this.certificadoRepository = certificadoRepository;
    }

    @Override
    public ValidacionResponse validarCertificado(ValidarCertificadoRequest request) {
        log.info("Validando certificado: {} con tipo: {}",
            request.getCodigoValidacion(), request.getTipoValidador());

        try {
            ValidacionDomainEntity validacionCreada = validarCertificadoUseCase.execute(request);

            log.info("Validación creada exitosamente. Tipo validador: {}",
                validacionCreada.getTipoValidador());

            return convertirAValidacionResponse(validacionCreada);

        } catch (Exception e) {
            log.error("Error al validar certificado {}: {}",
                request.getCodigoValidacion(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ValidacionResponse> obtenerValidacionesCertificado(String codigoValidacion) {
        log.debug("Obteniendo validaciones para certificado: {}", codigoValidacion);

        List<ValidacionDomainEntity> validaciones =
            obtenerValidacionesCertificadoUseCase.execute(codigoValidacion);

        return validaciones.stream()
            .map(this::convertirAValidacionResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ValidacionResponse> obtenerValidacionesPorTipo(String tipoValidador) {
        log.debug("Obteniendo validaciones por tipo: {}", tipoValidador);
        return List.of(); // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public List<ValidacionResponse> obtenerValidacionesPorResultado(String resultado) {
        log.debug("Obteniendo validaciones por resultado: {}", resultado);
        return List.of(); // Placeholder
    }

    private ValidacionResponse convertirAValidacionResponse(ValidacionDomainEntity validacion) {
        ValidacionResponse response = new ValidacionResponse();

        // ID temporal (tu lógica)
        if (validacion.getCertificado() != null && validacion.getTipoValidador() != null) {
            response.setIdValidacion(validacion.getCertificado() + "_" + validacion.getTipoValidador());
        }

        response.setCodigoValidacion(validacion.getCertificado());
        response.setTipoValidador(validacion.getTipoValidador());
        response.setFechaValidacion(validacion.getFechaValidacion());
        response.setResultado(validacion.getResultado());
        response.setDetalle(validacion.getDetalle());

        // ✅ Buscar certificado por CÓDIGO (NO por ID)
        if (validacion.getCertificado() != null) {
            // Recomendado: define este método en tu repo si aún no existe:
            // Optional<CertificadoDomainEntity> findByCodigoValidacion(String codigoValidacion);
            certificadoRepository.findByCodigoValidacion(validacion.getCertificado())
                .ifPresent(certificado -> response.setCertificado(convertirCertificadoAResponse(certificado)));
        }

        return response;
    }

    // ✅ Mapper interno (evita depender de CertificadoServiceImpl)
    private CertificadoResponse convertirCertificadoAResponse(CertificadoDomainEntity certificado) {
        CertificadoResponse response = new CertificadoResponse();
        response.setIdCertificado(certificado.getIdCertificado());
        response.setAsistenciaId(certificado.getAsistenciaId());
        response.setCodigoValidacion(certificado.getCodigoValidacion());
        response.setFechaEmision(certificado.getFechaEmision());
        response.setEstado(certificado.getEstado() != null ? certificado.getEstado().getCodigo() : null);
        response.setUrlPdf(certificado.getUrlPdf());
        response.setCreatedAt(certificado.getCreatedAt());
        response.setUpdatedAt(certificado.getUpdatedAt());
        return response;
    }
}
