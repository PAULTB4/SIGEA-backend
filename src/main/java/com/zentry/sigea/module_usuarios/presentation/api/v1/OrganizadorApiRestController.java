package com.zentry.sigea.module_usuarios.presentation.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.services.AsistenciaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios/organizador")
public class OrganizadorApiRestController {
    
    private final AsistenciaService asistenciaService;

    public OrganizadorApiRestController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @PostMapping("/registrar-asistencia")
    @PreAuthorize("hasRole('ORGANIZADOR')")
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
                .body("Error al registrar asistencia: " + e.getMessage());
        }        
    }
}
