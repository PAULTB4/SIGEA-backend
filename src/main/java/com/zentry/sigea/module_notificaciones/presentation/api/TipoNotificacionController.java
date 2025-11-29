package com.zentry.sigea.module_notificaciones.presentation.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.ITipoNotificacionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controlador REST para gestionar tipos de notificación
 * Permite CRUD de catálogo de tipos
 */
@RestController
@RequestMapping("/api/v1/tipos-notificacion")
@CrossOrigin(origins = "*")
public class TipoNotificacionController {
    
    private final ITipoNotificacionRepository tipoRepository;

    public TipoNotificacionController(ITipoNotificacionRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
    }

    /**
     * Crear un nuevo tipo de notificación
     * POST /api/v1/tipos-notificacion
     */
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear un nuevo tipo de notificación.",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Crear"}
    )
    public ResponseEntity<?> crearTipo(@RequestBody TipoRequest request) {
        try {
            if (request.getCodigo() == null || request.getCodigo().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El código es obligatorio");
            }
            if (request.getEtiqueta() == null || request.getEtiqueta().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La etiqueta es obligatoria");
            }

            TipoNotificacionDomainEntity tipo = TipoNotificacionDomainEntity.create(
                request.getCodigo().toUpperCase(),
                request.getEtiqueta()
            );
            
            TipoNotificacionDomainEntity tipoGuardado = tipoRepository.save(tipo);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(new TipoResponse(tipoGuardado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear tipo: " + e.getMessage());
        }
    }

    /**
     * Listar todos los tipos de notificación
     * GET /api/v1/tipos-notificacion
     */
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Listar todos los tipos de notificación.",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Listar"}
    )
    public ResponseEntity<List<TipoResponse>> listarTipos() {
        try {
            List<TipoNotificacionDomainEntity> tipos = tipoRepository.findAll();
            List<TipoResponse> response = tipos.stream()
                .map(TipoResponse::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener un tipo por ID
     * GET /api/v1/tipos-notificacion/{id}
     */
    @GetMapping("/obtener/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Obtener un tipo de notificacion por su ID.",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Obtener"}
    )
    public ResponseEntity<?> obtenerTipo(@PathVariable String id) {
        try {
            return tipoRepository.findById(id)
                .map(tipo -> ResponseEntity.ok(new TipoResponse(tipo)))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener tipo: " + e.getMessage());
        }
    }

    /**
     * Buscar tipo por código
     * GET /api/v1/tipos-notificacion/codigo/{codigo}
     */
    @GetMapping("/obtener/codigo/{codigo}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Obtener un tipo de notificacion por su codigo.",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Obtener"}
    )
    public ResponseEntity<?> obtenerTipoPorCodigo(@PathVariable String codigo) {
        try {
            return tipoRepository.findByCodigo(codigo.toUpperCase())
                .map(tipo -> ResponseEntity.ok(new TipoResponse(tipo)))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar tipo: " + e.getMessage());
        }
    }

    /**
     * Eliminar un tipo
     * DELETE /api/v1/tipos-notificacion/{id}
     */
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Eliminar un tipo de notificacion por su ID.",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Eliminar"}
    )
    public ResponseEntity<?> eliminarTipo(@PathVariable String id) {
        try {
            if (!tipoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            tipoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar tipo: " + e.getMessage());
        }
    }

    /**
     * Health check
     * GET /api/v1/tipos-notificacion/health
     */
    @GetMapping("/health")
    @Operation(
        summary = "Verificar el funcionamiento del controlador tipo de notificacion",
        tags = {"Health"}
    )
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Tipos Notificación API is running");
    }

    // ========== DTOs ==========
    
    static class TipoRequest {
        private String codigo;
        private String etiqueta;

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public void setEtiqueta(String etiqueta) {
            this.etiqueta = etiqueta;
        }
    }

    static class TipoResponse {
        private String id;
        private String codigo;
        private String etiqueta;

        public TipoResponse(TipoNotificacionDomainEntity tipo) {
            this.id = tipo.getId();
            this.codigo = tipo.getCodigo();
            this.etiqueta = tipo.getEtiqueta();
        }

        public String getId() {
            return id;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getEtiqueta() {
            return etiqueta;
        }
    }
}
