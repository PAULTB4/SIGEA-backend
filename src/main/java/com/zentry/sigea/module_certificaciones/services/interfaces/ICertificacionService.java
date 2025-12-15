
package com.zentry.sigea.module_certificaciones.services.interfaces;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;

/**
 * Interfaz para el servicio de certificaciones
 * Maneja la lógica de negocio para certificados y validaciones
 */
public interface ICertificacionService {

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
     * @param request Datos para crear el certificado
     * @return Certificado creado
     */
    CertificadoResponse crearCertificado(CrearCertificadoRequest request);
    
    /**
     * Crea certificados masivos con una sola peticion
     * @param listAsistenciaIds Conjunto de datos para crear los certificados
     * @return IdCerttificado, Resultado
     */
    Map<String , Boolean> crearCertificadosMasivos(List<String> listAsistenciaIds);

    /**
     * Busca un certificado por su código de validación
     * @param codigoValidacion Código único del certificado
     * @return Optional con el certificado encontrado
     */
    Optional<CertificadoResponse> buscarCertificadoPorCodigo(String codigoValidacion);
    
    /**
     * Busca certificados por ID de inscripción
     * @param inscripcionId ID de la inscripción
     * @return Optional con el certificado de la inscripción
     */
    Optional<CertificadoResponse> buscarCertificadoPorInscripcion(Long inscripcionId);
    
    /**
     * Obtiene todos los certificados
     * @return Lista de todos los certificados
     */
    List<CertificadoResponse> obtenerTodosCertificados();
    
    /**
     * Obtiene certificados por estado
     * @param codigoEstado Código del estado (EMITIDO, REVOCADO, SUSPENDIDO)
     * @return Lista de certificados con ese estado
     */
    List<CertificadoResponse> obtenerCertificadosPorEstado(String codigoEstado);
    
    /**
     * Valida un certificado
     * @param request Datos para la validación
     * @return Resultado de la validación
     */
    ValidacionResponse validarCertificado(ValidarCertificadoRequest request);
    
    /**
     * Obtiene las validaciones de un certificado
     * @param codigoValidacion Código del certificado
     * @return Lista de validaciones del certificado
     */
    List<ValidacionResponse> obtenerValidacionesCertificado(String codigoValidacion);
    
    /**
     * Revoca un certificado
     * @param codigoValidacion Código del certificado a revocar
     * @param motivo Motivo de la revocación
     * @return Certificado revocado
     */
    CertificadoResponse revocarCertificado(String codigoValidacion, String motivo);
    
    /**
     * Reactiva un certificado suspendido
     * @param codigoValidacion Código del certificado a reactivar
     * @return Certificado reactivado
     */
    CertificadoResponse reactivarCertificado(String codigoValidacion);
    
    /**
     * Genera el PDF del certificado
     * @param codigoValidacion Código del certificado
     * @return URL del PDF generado
     */
    String generarPdfCertificado(String codigoValidacion);

}