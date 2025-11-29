package com.zentry.sigea.module_actividad.presentation.api;
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

import com.zentry.sigea.module_actividad.presentation.models.requestDTO.ActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.CrearActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.ActividadResponse;
import com.zentry.sigea.module_actividad.services.ActividadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para gestionar actividades
 * Implementa la capa de presentación siguiendo Clean Architecture
 */
@RestController
@RequestMapping("/api/v1/actividades")
@CrossOrigin(origins = "*")
public class ActividadController {
    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    /**
     * Crear una nueva actividad
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear actividad",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Crear"}
    )
    public ResponseEntity<String> crearActividad(@RequestBody CrearActividadRequest request) {
        try {
            // Ejecutar el caso de uso a través del servicio pasando el request con IDs
            String responseMessage = actividadService.crearActividad(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener una actividad por ID
     */
    @GetMapping("/obtener/{id}")
    @Operation(
        summary = "Obtener una actividad por su ID",
        tags = {"Obtener"}
    )
    public ResponseEntity<ActividadResponse> obtenerActividad(@PathVariable String id) {
        return actividadService.obtenerActividadPorId(id) != null ? 
                    ResponseEntity.ok(actividadService.obtenerActividadPorId(id)) : 
                    ResponseEntity.notFound().build();
    }

    /**
     * Listar actividades con filtros opcionales
     */
    @GetMapping("/listar")
    @Operation(
        summary = "Listar actividades",
        tags = {"Listar"}
    )
    public ResponseEntity<List<ActividadResponse>> listarActividades() {
        
        // Implementar lógica de filtros cuando sea necesario
        List<ActividadResponse> actividades = actividadService.listarActividades();

        return ResponseEntity.ok(actividades);
        
    }

    /**
     * Endpoint de salud para verificar que el controlador funciona
     */
    @GetMapping("/health")
    @Operation(
        summary = "Verificar funcionamiento del modulo actividad",
        tags = {"Health"}
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Actividades API is running");
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Eliminar una actividad por su ID",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Eliminar"}
    )
    public ResponseEntity<Void> eliminarActividad(@PathVariable String id) {
        try {
            actividadService.eliminarActividad(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualizar una actividad existente
     */
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Actualizar una actividad por su ID",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Actualizar"}
    )
    public ResponseEntity<ActividadResponse> actualizarActividad(@PathVariable String id, @RequestBody ActividadRequest request) {
        try {
            ActividadResponse actividadActualizada = actividadService.actualizarActividad(id, request);
            return ResponseEntity.ok(actividadActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}