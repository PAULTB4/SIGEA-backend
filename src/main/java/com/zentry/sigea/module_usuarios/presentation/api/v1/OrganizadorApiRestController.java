package com.zentry.sigea.module_usuarios.presentation.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_usuarios.presentation.models.mappers.RegistrarAsistenciaMapper;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarAsistenciaRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.GeneralResponseDTO;
import com.zentry.sigea.module_usuarios.services.OrganizadorService;
import com.zentry.sigea.security.UsuarioAuthInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/usuarios/organizador")
@CrossOrigin(origins = "*")
public class OrganizadorApiRestController {
    
    private final OrganizadorService organizadorService;

    public OrganizadorApiRestController(OrganizadorService organizadorService){
        this.organizadorService = organizadorService;
    }

    @GetMapping({"/dashboard" , "/"})
    @PreAuthorize("hasRole('ROLE_ORGANIZADOR')")
    @Operation(
        summary = "Home de organizador",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Home"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> indexOrganizador(
        @AuthenticationPrincipal UsuarioAuthInfo usuarioAuthInfo
    ) {

        Map<String , String> dataForIndexOrganizador = new HashMap<>();

        dataForIndexOrganizador.put("usuarioId", usuarioAuthInfo.getId());
        dataForIndexOrganizador.put("correo", usuarioAuthInfo.getCorreo());

        return ResponseEntity.status(HttpStatus.OK).body(
            new GeneralResponseDTO<>(
                true, 
                "Operacion exitosa", 
                dataForIndexOrganizador
            )
        );
    }
    

    @PostMapping("/registrar-asistencia")
    @PreAuthorize("hasRole('ROLE_ORGANIZADOR')")
    @Operation(
        summary = "Registrar asistencia de los participantes",
        security = @SecurityRequirement(
            name = "organizadorJWT"
            ),
        tags = {"Registrar"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> registrarAsistenciaMasiva(
        @RequestBody RegistrarAsistenciaRequestDTO registrarAsistenciaRequestDTO
    ) {
        try {
            String registrarAsistenciaMessage = organizadorService.registrarAsistencia(
                RegistrarAsistenciaMapper.requestToService(registrarAsistenciaRequestDTO)
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(
                new GeneralResponseDTO<>(
                    true, 
                    registrarAsistenciaMessage, 
                    null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    false, 
                    "Ocurrio un errror al procesar la solicitud.", 
                    null
                )
            );
        }        
    }
}
