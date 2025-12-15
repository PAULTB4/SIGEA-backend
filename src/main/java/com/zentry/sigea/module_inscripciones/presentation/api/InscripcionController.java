package com.zentry.sigea.module_inscripciones.presentation.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.InscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.InscripcionResponse;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.ApiResponse;
import com.zentry.sigea.module_inscripciones.services.InscripcionService;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


/**
 * Controlador REST para gestionar inscripciones
 * Implementa la capa de presentación siguiendo Clean Architecture
 */
@RestController
@RequestMapping("/api/v1/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {
    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    /**
     * Obtener una inscripción por ID
     */
    @GetMapping("/obtener/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR' , 'ROLE_ORGANIZADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Obtener inscipcion por ID.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<InscripcionResponse> obtenerInscripcion(@PathVariable String id) {
        try {
            InscripcionResponse inscripcion = inscripcionService.obtenerInscripcionPorId(id);
            return ResponseEntity.ok(inscripcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Listar todas las inscripciones
     */
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR' , 'ROLE_ORGANIZADOR')")
    @Operation(
        summary = "Listar las inscipciones.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Listar"}
    )
    public ResponseEntity<List<InscripcionResponse>> listarInscripciones() {
        List<InscripcionResponse> inscripciones = inscripcionService.listarInscripciones();
        return ResponseEntity.ok(inscripciones);
    }

    /*
    * Crear inscripcion se maneja desde el servicio de actividades
    */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANTE', 'ROLE_ORGANIZADOR', 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear una nueva inscripción.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Crear"}
    )
    public ResponseEntity<?> crearInscripcion(@RequestBody @Valid InscripcionRequest inscripcionRequest) {
        try {
            CrearInscripcionServiceDTO serviceDTO = convertirAServiceDTO(inscripcionRequest);
            inscripcionService.crearInscripcion(serviceDTO);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Inscripción creada exitosamente", true));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error interno del servidor", false));
        }
    }

    /**
     * Obtener inscripciones por usuario
     */
    @GetMapping("/obtener/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANTE', 'ROLE_ORGANIZADOR', 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "obtener inscripciones por ID de usuario.",
        security = {
            @SecurityRequirement(name = "participanteJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "administradorJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<List<InscripcionResponse>> obtenerInscripcionesPorUsuario(
        @PathVariable String usuarioId
    ) {
        try {
            List<InscripcionResponse> inscripciones = 
                inscripcionService.obtenerInscripcionesPorUsuario(usuarioId);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener inscripciones por actividad
     */
    @GetMapping("/obtener/actividad/{actividadId}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "obtener inscripciones por ID de actividad.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<List<InscripcionResponse>> obtenerInscripcionesPorActividad(
        @PathVariable String actividadId
    ) {
        try {
            List<InscripcionResponse> inscripciones = 
                inscripcionService.obtenerInscripcionesPorActividad(actividadId);
            return ResponseEntity.ok(inscripciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualizar una inscripción
     */
    @PutMapping("actualizar/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Actualizar inscripcion por su ID.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Actualizar"}
    )
    public ResponseEntity<InscripcionResponse> actualizarInscripcion(
        @PathVariable String id,
        @RequestBody InscripcionRequest request
    ) {
        try {
            InscripcionResponse inscripcion = inscripcionService.actualizarInscripcion(id, request);
            return ResponseEntity.ok(inscripcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar una inscripción
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR' , 'ROLE_ADMINISTRADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Eliminar inscripcion por su ID.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Eliminar"}
    )
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable String id) {
        try {
            inscripcionService.eliminarInscripcion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint de salud para verificar que el controlador funciona
     */
    @GetMapping("/health")
    @Operation(
        summary = "Verificar funcionamiento del controlador de inscripciones.",
        tags = {"Health"}
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Inscripciones API is running");
    }


    private CrearInscripcionServiceDTO convertirAServiceDTO(InscripcionRequest request) {
    CrearInscripcionServiceDTO serviceDTO = new CrearInscripcionServiceDTO();
    serviceDTO.setUsuarioId(request.getUsuarioId());
    serviceDTO.setActividadId(request.getActividadId());
    serviceDTO.setFechaInscripcion(request.getFechaInscripcion());
    serviceDTO.setEstadoId(request.getEstadoId());
    return serviceDTO;
}
}

