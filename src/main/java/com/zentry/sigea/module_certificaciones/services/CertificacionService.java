    package com.zentry.sigea.module_certificaciones.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.ICertificacionService;
import com.zentry.sigea.module_certificaciones.services.interfaces.ICertificadoService;
import com.zentry.sigea.module_certificaciones.services.interfaces.IValidacionService;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CertificacionService implements ICertificacionService {
    private static final Logger log = LoggerFactory.getLogger(CertificacionService.class);

    private final ICertificadoService certificadoService;
    private final IValidacionService validacionService;

    public CertificacionService(
        ICertificadoService certificadoService,
        IValidacionService validacionService
    ) {
        this.certificadoService = certificadoService;
        this.validacionService = validacionService;
    }

    @Override
    public CertificadoResponse crearCertificadoConArchivo(CrearCertificadoRequest request, MultipartFile file) {
        return certificadoService.crearCertificadoConArchivo(request, file);
    }
    
    @Override
    public CertificadoResponse crearCertificado(CrearCertificadoRequest request) {
        log.info("Delegando creación de certificado a servicio Clean Architecture");
        return certificadoService.crearCertificado(request);
    }
    
    @Override
    public Map<String , Boolean> crearCertificadosMasivos(List<String> listActividadIds){
        log.info("Delegando creación de certificados masivos a servicio Clean Architecture");
        return certificadoService.crearCertificadosMasivos(listActividadIds);
    }

    @Override
    public Optional<CertificadoResponse> buscarCertificadoPorCodigo(String codigoValidacion) {
        log.debug("Delegando búsqueda por código a servicio Clean Architecture");
        return certificadoService.buscarCertificadoPorCodigo(codigoValidacion);
    }
    
    @Override
    public Optional<CertificadoResponse> buscarCertificadoPorInscripcion(Long inscripcionId) {
        log.debug("Delegando búsqueda por inscripción a servicio Clean Architecture");
        return certificadoService.buscarCertificadoPorInscripcion(inscripcionId.toString());
    }
    
    @Override
    public List<CertificadoResponse> obtenerTodosCertificados() {
        log.debug("Delegando obtención de todos los certificados a servicio Clean Architecture");
        return certificadoService.obtenerTodosCertificados();
    }
    
    @Override
    public List<CertificadoResponse> obtenerCertificadosPorEstado(String codigoEstado) {
        log.debug("Delegando búsqueda por estado a servicio Clean Architecture");
        return certificadoService.obtenerCertificadosPorEstado(codigoEstado);
    }
    
    @Override
    public ValidacionResponse validarCertificado(ValidarCertificadoRequest request) {
        log.info("Delegando validación de certificado a servicio Clean Architecture");
        return validacionService.validarCertificado(request);
    }
    
    @Override
    public List<ValidacionResponse> obtenerValidacionesCertificado(String codigoValidacion) {
        log.debug("Delegando obtención de validaciones a servicio Clean Architecture");
        return validacionService.obtenerValidacionesCertificado(codigoValidacion);
    }
    
    @Override
    public CertificadoResponse revocarCertificado(String codigoValidacion, String motivo) {
        log.info("Delegando revocación de certificado a servicio Clean Architecture");
        return certificadoService.revocarCertificado(codigoValidacion, motivo);
    }
    
    @Override
    public CertificadoResponse reactivarCertificado(String codigoValidacion) {
        log.info("Delegando reactivación de certificado a servicio Clean Architecture");
        return certificadoService.reactivarCertificado(codigoValidacion);
    }
    
    @Override
    public String generarPdfCertificado(String codigoValidacion) {
        log.info("Delegando generación de PDF a servicio Clean Architecture");
        return certificadoService.generarPdfCertificado(codigoValidacion);
    }

    @Override
    public String subirCertificado(MultipartFile file, String pathDestino) throws Exception {
        return certificadoService.subirCertificado(file, pathDestino);
    }
}