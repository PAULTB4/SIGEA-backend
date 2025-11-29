package com.zentry.sigea.module_usuarios.presentation.api.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_usuarios.presentation.models.mappers.CrearRolMapper;
import com.zentry.sigea.module_usuarios.presentation.models.mappers.RegistrarUsuarioMapper;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.CrearRolRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarUsuarioRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.GeneralResponseDTO;
import com.zentry.sigea.module_usuarios.services.AdministradorService;
import com.zentry.sigea.security.UsuarioAuthInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/usuarios/administrador")
@CrossOrigin(origins = "*") // El frontend estara en "http://localhost:16000" en local
public class AdministradorApiRestController {
    
    private final AdministradorService administradorService;

    public AdministradorApiRestController(
        AdministradorService administradorService
    ){
        this.administradorService = administradorService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Home de administrador",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Home"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> indexAdministrador(
        @AuthenticationPrincipal UsuarioAuthInfo usuarioAuthInfo
    ) {
        Map<String , String> dataForIndexAdministrador = new HashMap<>();

        dataForIndexAdministrador.put("usuarioId", usuarioAuthInfo.getId());
        dataForIndexAdministrador.put("correo", usuarioAuthInfo.getCorreo());

        return ResponseEntity.status(HttpStatus.OK).body(
            new GeneralResponseDTO<>(
                true, 
                "Operacion exitosa", 
                dataForIndexAdministrador
            )
        );
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Registrar usuario",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Crear"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> registerUsuario(
        @RequestBody RegistrarUsuarioRequestDTO registrarUsuarioRequestDTO
    ) {
        try {
            String registerMessage = administradorService.registerUsuario(
                RegistrarUsuarioMapper.requestToDomain(registrarUsuarioRequestDTO) , 
                registrarUsuarioRequestDTO.getRolId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(
                new GeneralResponseDTO<>(
                    true, 
                    registerMessage, 
                    null
                )
            );
        } catch (DataIntegrityViolationException d) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new GeneralResponseDTO<>(
                    false, 
                    "El correo especificado ya esta en uso.", 
                    null
                )
            );
        } catch (RuntimeException r) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new GeneralResponseDTO<>(
                    false, 
                    r.getMessage(), 
                    Map.of(
                    "Tipo de error", r.getClass().getName() ,
                        "Mensaje del error" , r.getMessage() , 
                        "Causa del error", r.getCause()
                    )
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    false, 
                    "Ocurrio un error al procesar la solicitud.", 
                    Map.of(
                    "Tipo de error", e.getClass().getName() ,
                        "Mensaje del error" , e.getMessage() , 
                        "Causa del error", e.getCause()
                    )
                )
            );
        } 
    }
    
    @PostMapping("/crear-rol")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @Operation(
        summary = "Crear roles",
        security = @SecurityRequirement(
            name = "administradorJWT"
            ),
        tags = {"Crear"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> crearRol(
        @Valid @RequestBody CrearRolRequestDTO crearRolRequestDTO , 
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String , String> errorMeessages = new HashMap<>();

            bindingResult.getFieldErrors().forEach((errorField) -> {
                errorMeessages.put(errorField.getField(), errorField.getDefaultMessage());
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponseDTO<>(
                    false , 
                    "Hay errores en los parametros" , 
                    errorMeessages
                )
            );
        }

        try {
            String crearRolMessage = administradorService.crearRol(
                CrearRolMapper.requestToDomain(crearRolRequestDTO)
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new GeneralResponseDTO<>(
                    true, 
                    crearRolMessage, 
                    null
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    false, 
                    "Ocurrio un error al procesar la solicitud", 
                    null
                )
            );
        }
    }    
}
