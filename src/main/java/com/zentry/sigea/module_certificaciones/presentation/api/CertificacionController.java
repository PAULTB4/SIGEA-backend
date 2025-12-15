package com.zentry.sigea.module_certificaciones.presentation.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.ICertificacionService;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.TipoInformeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/api/v1/certificaciones")
@CrossOrigin(origins = "*")
public class CertificacionController {

    private final TipoInformeMapper tipoInformeMapper;
    
    private static final Logger log = LoggerFactory.getLogger(CertificacionController.class);
    
    @Autowired
    private ICertificacionService certificacionService;

    /**
     * Subir archivo de certificado a proveedor externo (Supabase, S3, etc)
     */
    @PostMapping("/subir-archivo")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Subir archivo de certificado",
        description = "Sube un archivo PDF de certificado al proveedor externo y retorna la URL",
        security = @SecurityRequirement(name = "administradorJWT"),
        tags = {"Subir"}
    )
    public ResponseEntity<String> subirArchivoCertificado(
            @RequestPart("file") MultipartFile file,
            @RequestParam("pathDestino") String pathDestino) {
        try {
            String url = certificacionService.subirCertificado(file, pathDestino);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("Error al subir archivo de certificado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    CertificacionController(TipoInformeMapper tipoInformeMapper) {
        this.tipoInformeMapper = tipoInformeMapper;
    }
    
    /**
     * Crear un nuevo certificado
     */
    @PostMapping(value = "/crear", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear certificado",
        description = "Crea un nuevo certificado para una inscripción. Permite subir un archivo o generarlo automáticamente.",
        security = @SecurityRequirement(name = "administradorJWT"),
        tags = {"Crear"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Certificado creado exitosamente"),
    })
    public ResponseEntity<CertificadoResponse> crearCertificado(
            @Parameter(
                description = "Datos del certificado en formato JSON.\n\nAtributos esperados:\n- asistenciaId: (string) ID de la asistencia.\n- tipoCertificado: (string) Tipo de certificado (por ejemplo, 'SUBIDO').\n- observaciones: (string, opcional) Observaciones adicionales.\n- nombreArchivo: (string, opcional) Nombre del archivo PDF.\n\nEjemplo:\n{\n  \"asistenciaId\": \"0e62115a-ee1e-4ade-8980-2fdf58382ff4\",\n  \"tipoCertificado\": \"SUBIDO\",\n  \"observaciones\": \"Texto opcional\",\n  \"nombreArchivo\": \"certificadofiis.pdf\"\n}",
                example = "{\n  \"asistenciaId\": \"0e62115a-ee1e-4ade-8980-2fdf58382ff4\",\n  \"tipoCertificado\": \"SUBIDO\",\n  \"observaciones\": \"Texto opcional\",\n  \"nombreArchivo\": \"certificadofiis.pdf\"\n}"
            )
            @RequestPart("datos") String datos,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CrearCertificadoRequest request = mapper.readValue(datos, CrearCertificadoRequest.class);
            log.info("Solicitud de creación de certificado para inscripción: {}", request.getAsistenciaId());

            CertificadoResponse certificado;
            if ("SUBIDO".equalsIgnoreCase(request.getTipoCertificado())) {
                if (file == null || file.isEmpty()) {
                    throw new IllegalArgumentException("Debe adjuntar un archivo para tipo SUBIDO");
                }
                // Lógica para subir el archivo y crear el certificado con la URL
                certificado = certificacionService.crearCertificadoConArchivo(request, file);
            } else {
                // Lógica para generar automáticamente el certificado
                certificado = certificacionService.crearCertificado(request);
            }
            log.info("Certificado creado exitosamente con ID: {}", certificado.getIdCertificado());
            return ResponseEntity.status(HttpStatus.CREATED).body(certificado);
        } catch (RuntimeException e) {
            log.error("Error al crear certificado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Error al deserializar datos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping("/crear/masivo/")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear certificados masivos.", 
        description = "Crea varios certificados en una sola peticion" , 
        security = @SecurityRequirement(
            name = "administradorJWT"
        ) , 
        tags = {"Crear"}
    )
    public ResponseEntity<Map<String , Boolean>> crearCertificadosMasivos(
        @RequestParam(name = "listAsistenciaIds") @NotEmpty(message = "Debe proporcionar la lista de asistencias") List<String> listAsistenciaIds
    ) {
        log.info("Solicitud de creación de certificado para la inscripcion");

        try {
            Map<String , Boolean> response = certificacionService.crearCertificadosMasivos(listAsistenciaIds);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al crear los certificados");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    

    /**
     * Buscar certificado por código de validación
     */
    @GetMapping("obtener/cod-validacion/{codigoValidacion}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR' , 'ROLE_PARTICIPANTE' , 'ROLE_ORGANIZADOR')")
    @Operation(
        summary = "Obtener certificado por código", 
        description = "Obtiene un certificado por su código de validación" , 
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "participanteJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<?> buscarCertificadoPorCodigo(
            @Parameter(description = "Código de validación del certificado") 
            @PathVariable String codigoValidacion) {
        log.debug("Buscando certificado por código: {}", codigoValidacion);
        Optional<CertificadoResponse> certificado = certificacionService.buscarCertificadoPorCodigo(codigoValidacion);
        if (certificado.isPresent()) {
            return ResponseEntity.ok(certificado.get());
        } else {
            // Retornar mensaje indicando que el certificado está en validación
            String mensaje = "El certificado aún está en proceso de validación. Recibirá una notificación cuando esté disponible para descarga.";
            return ResponseEntity.status(202).body(mensaje);
        }
    }
    
    /**
     * Buscar certificado por inscripción
     */
    @GetMapping("obtener/inscripcion/{inscripcionId}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Obtener certificado por inscripción", 
        description = "Obtiene el certificado de una inscripción" , 
        security = @SecurityRequirement(
            name = "participanteJWT"
        ),
        tags = {"Obtener"}
    )
    public ResponseEntity<CertificadoResponse> buscarCertificadoPorInscripcion(
            @Parameter(description = "ID de la inscripción") 
            @PathVariable Long inscripcionId) {
        
        log.debug("Buscando certificado por inscripción: {}", inscripcionId);
        
        Optional<CertificadoResponse> certificado = certificacionService.buscarCertificadoPorInscripcion(inscripcionId);
        
        return certificado
            .map(cert -> ResponseEntity.ok(cert))
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener todos los certificados
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Listar certificados", 
        description = "Lista todos los certificados" , 
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Listar"}
    )
    public ResponseEntity<List<CertificadoResponse>> obtenerTodosCertificados() {
        log.debug("Obteniendo todos los certificados");
        
        List<CertificadoResponse> certificados = certificacionService.obtenerTodosCertificados();
        return ResponseEntity.ok(certificados);
    }
    
    /**
     * Obtener certificados por estado
     */
    @GetMapping("/obtener/estado/{codigoEstado}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Obtener certificados por estado", 
        description = "Obtiene certificados filtrados por estado" , 
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "participanteJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<List<CertificadoResponse>> obtenerCertificadosPorEstado(
            @Parameter(description = "Código del estado (EMITIDO, REVOCADO, SUSPENDIDO)") 
            @PathVariable String codigoEstado) {
        
        log.debug("Buscando certificados por estado: {}", codigoEstado);
        
        try {
            List<CertificadoResponse> certificados = certificacionService.obtenerCertificadosPorEstado(codigoEstado);
            return ResponseEntity.ok(certificados);
        } catch (RuntimeException e) {
            log.error("Error al buscar certificados por estado: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Validar un certificado
     */
    @PostMapping("/validar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Validar certificado", 
        description = "Realiza la validación de un certificado" , 
        security = @SecurityRequirement(name = "administradorJWT"),
        tags = {"Validar"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Certificado validado exitosamente"),
    })
    public ResponseEntity<ValidacionResponse> validarCertificado(@Valid @RequestBody ValidarCertificadoRequest request) {
        log.info("Validando certificado: {} con tipo: {}", request.getCodigoValidacion(), request.getTipoValidador());
        
        try {
            ValidacionResponse validacion = certificacionService.validarCertificado(request);
            log.info("Certificado validado exitosamente: {}", request.getCodigoValidacion());
            return ResponseEntity.ok(validacion);
        } catch (RuntimeException e) {
            log.error("Error al validar certificado: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener validaciones de un certificado
     */
    @GetMapping("/obtener/validaciones/{codigoValidacion}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Obtener validaciones", 
        description = "Obtiene las validaciones de un certificado" , 
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "participanteJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<List<ValidacionResponse>> obtenerValidacionesCertificado(
            @Parameter(description = "Código de validación del certificado") 
            @PathVariable String codigoValidacion) {
        
        log.debug("Obteniendo validaciones para certificado: {}", codigoValidacion);
        
        try {
            List<ValidacionResponse> validaciones = certificacionService.obtenerValidacionesCertificado(codigoValidacion);
            return ResponseEntity.ok(validaciones);
        } catch (RuntimeException e) {
            log.error("Error al obtener validaciones: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Revocar un certificado
     */
    @PutMapping("/revocar/{codigoValidacion}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Revocar certificado", 
        description = "Revoca un certificado especificando el motivo" , 
        security = @SecurityRequirement(name = "administradorJWT"),
        tags = {"Revocar"}
    )
    public ResponseEntity<CertificadoResponse> revocarCertificado(
            @Parameter(description = "Código de validación del certificado") 
            @PathVariable String codigoValidacion,
            @Parameter(description = "Motivo de la revocación") 
            @RequestParam(defaultValue = "Revocación administrativa") String motivo) {
        
        log.info("Revocando certificado: {} por motivo: {}", codigoValidacion, motivo);
        
        try {
            CertificadoResponse certificado = certificacionService.revocarCertificado(codigoValidacion, motivo);
            log.info("Certificado revocado exitosamente: {}", codigoValidacion);
            return ResponseEntity.ok(certificado);
        } catch (RuntimeException e) {
            log.error("Error al revocar certificado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Reactivar un certificado
     */
    @PutMapping("/reactivar/{codigoValidacion}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Reactivar certificado", 
        description = "Reactiva un certificado suspendido",
        security = @SecurityRequirement(name = "administradorJWT"),
        tags = {"Reactivar"}
    )
    public ResponseEntity<CertificadoResponse> reactivarCertificado(
            @Parameter(description = "Código de validación del certificado") 
            @PathVariable String codigoValidacion) {
        
        log.info("Reactivando certificado: {}", codigoValidacion);
        
        try {
            CertificadoResponse certificado = certificacionService.reactivarCertificado(codigoValidacion);
            log.info("Certificado reactivado exitosamente: {}", codigoValidacion);
            return ResponseEntity.ok(certificado);
        } catch (RuntimeException e) {
            log.error("Error al reactivar certificado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Generar PDF del certificado
     */
    @PostMapping("/generar-pdf/{codigoValidacion}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Generar PDF", 
        description = "Genera el archivo PDF del certificado" , 
        security = {
            @SecurityRequirement(name = "organizadorJWT") , 
            @SecurityRequirement(name = "administradorJWT")
        },
        tags = {"Generar"}
    )
    public ResponseEntity<String> generarPdfCertificado(
            @Parameter(description = "Código de validación del certificado") 
            @PathVariable String codigoValidacion) {
        
        log.info("Generando PDF para certificado: {}", codigoValidacion);
        
        try {
            String urlPdf = certificacionService.generarPdfCertificado(codigoValidacion);
            log.info("PDF generado exitosamente para certificado: {}", codigoValidacion);
            return ResponseEntity.ok(urlPdf);
        } catch (RuntimeException e) {
            log.error("Error al generar PDF: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    @Operation(
        summary = "Verificar el funcionamiento del modulo certificaciones.",
        tags = {"Health"}
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Asistencias API is running");
    }
}