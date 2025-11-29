package com.zentry.sigea.module_informe.presentation.api;

import com.zentry.sigea.module_informe.presentation.models.requestDTO.TipoInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.TipoInformeResponse;
import com.zentry.sigea.module_informe.services.TipoInformeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-informe")
@CrossOrigin(origins = "*")
public class TipoInformeController {

    private final TipoInformeService tipoInformeService;

    public TipoInformeController(TipoInformeService tipoInformeService) {
        this.tipoInformeService = tipoInformeService;
    }

    /**
     * Crear un nuevo tipo de informe
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear tipo de informe",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Crear"}
    )
    public ResponseEntity<TipoInformeResponse> createTipoInforme(@RequestBody TipoInformeRequest request) {
        try {
            TipoInformeResponse response = tipoInformeService.crearTipoInforme(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Listar todos los tipos de informe
     */
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Listar los tipos de informe",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Listar"}
    )
    public ResponseEntity<List<TipoInformeResponse>> listarTiposInforme() {
        List<TipoInformeResponse> response = tipoInformeService.listarTiposInforme();
        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar un tipo de informe por ID
     */
    @DeleteMapping("eliminar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Eliminar un tipo de informe",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Eliminar"}
    )
    public ResponseEntity<Void> eliminarTipoInforme(@PathVariable String id) {
        try {
            tipoInformeService.eliminarTipoInforme(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

