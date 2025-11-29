package com.zentry.sigea.module_sesiones.presentacion.api;

import com.zentry.sigea.module_sesiones.presentacion.models.CrearSesionRequest;
import com.zentry.sigea.module_sesiones.presentacion.models.SesionRequest;
import com.zentry.sigea.module_sesiones.presentacion.models.SesionResponse;
import com.zentry.sigea.module_sesiones.services.SesionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST para gestionar sesiones
 */
@RestController
@RequestMapping("/api/v1/sesiones")
@CrossOrigin(origins = "*")
public class SesionController {
    
    private final SesionService sesionService;

    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }


    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_ORGANIZAODR')")
    @Operation(
        summary = "Crear sesion",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Crear"}
    )
    public ResponseEntity<SesionResponse> crearSesion(@Valid @RequestBody CrearSesionRequest request) {
        try {
            SesionResponse sesionCreada = sesionService.crearSesion(request);
            return ResponseEntity
                .created(URI.create("/api/v1/sesiones/" + sesionCreada.getId()))
                .body(sesionCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ROLE_ORGANIZAODR')")
    @Operation(
        summary = "Listar sesiones",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Listar"}
    )
    public ResponseEntity<List<SesionResponse>> listarSesiones(
        @RequestParam(required = false) String actividadId
    ) {
        try {
            List<SesionResponse> sesiones;
            
            if (actividadId != null) {
                sesiones = sesionService.listarSesionesPorActividad(actividadId);
            } else {
                sesiones = sesionService.listarSesiones();
            }
            
            return ResponseEntity.ok(sesiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/obtener/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZAODR')")
    @Operation(
        summary = "Obtener una sesion por su ID",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Obtener"}
    )
    public ResponseEntity<SesionResponse> obtenerSesion(@PathVariable String id) {
        try {
            SesionResponse sesion = sesionService.obtenerSesionPorId(id);
            return ResponseEntity.ok(sesion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZAODR')")
    @Operation(
        summary = "Actualizar sesion por su ID",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Actualizar"}
    )
    public ResponseEntity<SesionResponse> actualizarSesion(
        @PathVariable String id,
        @Valid @RequestBody SesionRequest request
    ) {
        try {
            SesionResponse sesionActualizada = sesionService.actualizarSesion(id, request);
            return ResponseEntity.ok(sesionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("eliminar/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZAODR')")
    @Operation(
        summary = "Eliminar una sesion por su ID",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Eliminar"}
    )
    public ResponseEntity<Void> eliminarSesion(@PathVariable String id) {
        try {
            sesionService.eliminarSesion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}