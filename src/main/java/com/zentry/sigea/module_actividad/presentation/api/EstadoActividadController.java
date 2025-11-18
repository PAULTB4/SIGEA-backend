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

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.EstadoActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.EstadoActividadResponse;
import com.zentry.sigea.module_actividad.services.EstadoActividadService;



/*
 * Controlador para gestionar los estados de las actividades
 * 
 */

@RestController
@RequestMapping("/api/v1/estados-actividad")
@CrossOrigin(origins = "*")
public class EstadoActividadController {
    private final EstadoActividadService estadoActividadService;
    public EstadoActividadController(EstadoActividadService estadoActividadService) {
        this.estadoActividadService = estadoActividadService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEstadoActividad(@RequestBody EstadoActividadRequest request) {
        try {
            String responseMessage = estadoActividadService.crearEstadoActividad(request);
            return ResponseEntity.ok(responseMessage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EstadoActividadResponse>> listarEstadoActividad() {
        List<EstadoActividadDomainEntity> estadosActividad = estadoActividadService.listarEstadosActividad();

        List<EstadoActividadResponse> response = estadosActividad.stream()
            .map(EstadoActividadResponse::fromEntity)
            .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstadoActividad(@PathVariable String id) {
        try {
            estadoActividadService.eliminarEstadoActividad(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoActividadResponse> actualizarEstado(@PathVariable String id, @RequestBody EstadoActividadRequest request) {
        try {
            EstadoActividadDomainEntity estadoActualizado = estadoActividadService.actualizarEstadoActividad(request);
            EstadoActividadResponse response = EstadoActividadResponse.fromEntity(estadoActualizado);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
}
