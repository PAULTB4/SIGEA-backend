package com.zentry.sigea.module_asistencias.presentation.api;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.AsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;
import com.zentry.sigea.module_asistencias.services.AsistenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/asistencias")
@CrossOrigin(origins = "*")
public class AsistenciaController {
    
    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ROLE_ORGANIZADOR')")
    @Operation(
        summary = "Registrar asistencia.",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Registro"}
    )
    public ResponseEntity<String> registrarAsistencia(
        @Valid @RequestBody RegistrarAsistenciaRequest request
    ) {
        try {
            String mensaje = asistenciaService.registrarAsistencia(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }

    @GetMapping("/obtener/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR', 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Obtener asistencia por ID.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Obtener"}
    )
    public ResponseEntity<AsistenciaResponse> obtenerAsistencia(@PathVariable String id) {
        try {
            AsistenciaResponse asistencia = asistenciaService.obtenerAsistenciaPorId(id);
            return ResponseEntity.ok(asistencia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZADOR')")
    @Operation(
        summary = "Actualizar asistencia por ID.",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Actualizar"}
    )
    public ResponseEntity<AsistenciaResponse> actualizarAsistencia(
        @PathVariable String id,
        @Valid @RequestBody AsistenciaRequest request
    ) {
        try {
            AsistenciaResponse asistenciaActualizada =
                asistenciaService.actualizarEstadoAsistencia(id, request.getPresente());
            return ResponseEntity.ok(asistenciaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar/sesion/{sesionId}")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR', 'ROLE_ADMINISTRADOR', 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Listar asistencias por ID de sesion.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Listar"}
    )
    public ResponseEntity<List<AsistenciaResponse>> listarPorSesion(
        @PathVariable String sesionId
    ) {
        try {
            List<AsistenciaResponse> asistencias =
                asistenciaService.listarAsistenciasPorSesion(sesionId);
            return ResponseEntity.ok(asistencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar/inscripcion/{inscripcionId}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Listar asistencias por ID de inscripcion.",
        security = @SecurityRequirement(
            name = "participanteJWT"
        ),
        tags = {"Listar"}
    )
    public ResponseEntity<List<AsistenciaResponse>> listarPorInscripcion(
        @PathVariable String inscripcionId
    ) {
        try {
            List<AsistenciaResponse> asistencias =
                asistenciaService.listarAsistenciasPorInscripcion(inscripcionId);
            return ResponseEntity.ok(asistencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar/sesion/{sesionId}/presentes")
    @PreAuthorize("hasAnyRole('ROLE_ORGANIZADOR', 'ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Listar presentes en una sesion.",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
        },
        tags = {"Listar"}
    )
    public ResponseEntity<List<AsistenciaResponse>> listarPresentesPorSesion(
        @PathVariable String sesionId
    ) {
        try {
            List<AsistenciaResponse> presentes =
                asistenciaService.listarPresentesPorSesion(sesionId);
            return ResponseEntity.ok(presentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    @Operation(
        summary = "Verificar el funcionamiento del modulo asistencias.",
        tags = {"Health"}
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Asistencias API is running");
    }
}