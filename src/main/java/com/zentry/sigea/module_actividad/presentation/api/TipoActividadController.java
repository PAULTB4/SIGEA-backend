package com.zentry.sigea.module_actividad.presentation.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.TipoActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.TipoActividadResponse;
import com.zentry.sigea.module_actividad.services.TipoActividadService;



@RestController
@RequestMapping("/api/v1/tipos-actividad")
// cabecera para permitir solicitudes desde cualquier origen
@CrossOrigin(origins = "*")
public class TipoActividadController {

    private final TipoActividadService tipoActividadService;

    public TipoActividadController(TipoActividadService tipoActividadService) {
        this.tipoActividadService = tipoActividadService;
    }

    /**
     * Crear un nuevo tipo de actividad
     */
    @PostMapping("/create")
    public ResponseEntity<String>  createTipoActividad(@RequestBody TipoActividadRequest request) 
    {
        // Lógica para crear el tipo de actividad

        try {

            String responseMessage = tipoActividadService.crearTipoActividad(request);
            return ResponseEntity.ok(responseMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    /*
     * Listar todos los tipos de actividad
    */
    @GetMapping
    public ResponseEntity<List<TipoActividadResponse>> listarTiposActividad() {
        List<TipoActividadDomainEntity> tiposActividad = tipoActividadService.listarTiposActividad();
        List<TipoActividadResponse> response = tiposActividad.stream()
            .map(TipoActividadResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoActividad(@PathVariable String id) {
        try {
            tipoActividadService.eliminarTipoActividad(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoActividadResponse> actualizarTipoActividad(@PathVariable String id, @RequestBody TipoActividadRequest request) {
        try {
            TipoActividadDomainEntity tipoActualizado = tipoActividadService.actualizarTipoActividad(id, request);
            TipoActividadResponse response = TipoActividadResponse.fromEntity(tipoActualizado);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
}
