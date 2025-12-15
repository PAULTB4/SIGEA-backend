
    package com.zentry.sigea.module_certificaciones.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;

/**
 * Interfaz del servicio de certificados
 * Define los contratos para la gestión de certificados
 */
public interface ICertificadoService {

    /**
     * Crea un certificado a partir de un archivo subido (PDF/imagen).
     * @param request datos del certificado
     * @param file archivo a subir
     * @return CertificadoResponse con la URL pública
     */
    CertificadoResponse crearCertificadoConArchivo(CrearCertificadoRequest request, MultipartFile file);

    /**
     * Sube un archivo de certificado al proveedor externo y retorna la URL.
     * @param file archivo a subir
     * @param pathDestino ruta destino dentro del bucket
     * @return URL pública del archivo subido
     */
    String subirCertificado(MultipartFile file, String pathDestino) throws Exception;

    /**
     * Crea un nuevo certificado para una inscripción
     */
    CertificadoResponse crearCertificado(CrearCertificadoRequest request);
    
    Map<String , Boolean> crearCertificadosMasivos(List<String> listActividadIds); 

    /**
     * Busca un certificado por su código de validación
     */
    Optional<CertificadoResponse> buscarCertificadoPorCodigo(String codigoValidacion);
    
    /**
     * Busca un certificado por la inscripción asociada
     */
    Optional<CertificadoResponse> buscarCertificadoPorInscripcion(String inscripcionId);
    
    /**
     * Obtiene todos los certificados
     */
    List<CertificadoResponse> obtenerTodosCertificados();
    
    /**
     * Obtiene todos los certificados con un estado específico
     */
    List<CertificadoResponse> obtenerCertificadosPorEstado(String codigoEstado);
    
    /**
     * Revoca un certificado
     */
    CertificadoResponse revocarCertificado(String codigoValidacion, String motivo);
    
    /**
     * Reactiva un certificado previamente revocado
     */
    CertificadoResponse reactivarCertificado(String codigoValidacion);
    
    /**
     * Genera el PDF de un certificado
     */
    String generarPdfCertificado(String codigoValidacion);
}