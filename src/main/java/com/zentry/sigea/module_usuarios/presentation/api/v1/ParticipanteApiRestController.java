package com.zentry.sigea.module_usuarios.presentation.api.v1;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_inscripciones.presentation.models.mappers.CrearInscripcionMapper;
import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.CrearInscripcionRequest;
import com.zentry.sigea.module_inscripciones.services.InscripcionService;
import com.zentry.sigea.module_usuarios.presentation.models.mappers.RegistrarParticipanteMapper;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarParticipanteRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.GeneralResponseDTO;
import com.zentry.sigea.module_usuarios.services.ParticipanteService;
import com.zentry.sigea.security.UsuarioAuthInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/usuarios/participante")
@CrossOrigin(origins = "*")
public class ParticipanteApiRestController {

    private final InscripcionService inscripcionService;
    private final ParticipanteService participanteService;

    public ParticipanteApiRestController(
        InscripcionService inscripcionService , 
        ParticipanteService participanteService
    ){
        this.inscripcionService = inscripcionService;
        this.participanteService = participanteService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasRole('ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Home de participante.",
        security = @SecurityRequirement(
            name = "participanteJWT"
            ),
        tags = {"Home"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> indexParticipante(
        @AuthenticationPrincipal UsuarioAuthInfo usuarioAuthInfo
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new GeneralResponseDTO<>(
                true, 
                "Operacion exitosa",
                Map.of(
                    "correo" , usuarioAuthInfo.getCorreo() ,  
                    "id" , usuarioAuthInfo.getId()
                )
            )
        );
    }
    

    @PostMapping("/inscripcion")
    @PreAuthorize("hasRole('ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Registrar inscripcion en un evento.",
        security = @SecurityRequirement(
            name = "participanteJWT"
            ),
        tags = {"Registrar"}
    )
    public ResponseEntity<String> registrarInscripcion(@RequestBody CrearInscripcionRequest crearInscripcionRequest) {
        try {
            inscripcionService.crearInscripcion(
                CrearInscripcionMapper.requestToService(crearInscripcionRequest)
            );
            return ResponseEntity.status(HttpStatus.OK).body("Inscripcion realizada con exito!.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
    El nombre del esquema en Swagger no afecta la validación de JWT, solo la UI y documentación.
    Puedes combinar varios esquemas en un mismo endpoint si quieres que acepte cualquiera:
    
    @Operation(security = {
        @SecurityRequirement(name = "adminJWT"),
        @SecurityRequirement(name = "userJWT")
    })

    // Esto significa “el cliente puede enviar cualquiera de los dos tokens y será aceptado”.
    */

    @PostMapping("/registrar")
    @Operation(
        summary = "Registrar un usuario participante.",
        tags = {"Registrar"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> registrarParticipante(
        @Valid @RequestBody RegistrarParticipanteRequestDTO registrarParticipanteRequestDTO , 
        BindingResult bindingResult
    ){
        
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new GeneralResponseDTO<>(false, "Ya estás autenticado", null)
            );
        }

        if (bindingResult.hasErrors()) {
            Map<String , String> errorMesagges = new HashMap<>();

            bindingResult.getFieldErrors().forEach(
                (errorField) -> {
                    errorMesagges.put(errorField.getField(), errorField.getDefaultMessage());
                }
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponseDTO<>(
                    false, 
                    "Ocurrio un error al intentar registrar", 
                    errorMesagges
                )
            );
        }

        try {
            
            String registrarParticipanteMessage = participanteService.registrarParticipante(
                RegistrarParticipanteMapper.requestToDomain(registrarParticipanteRequestDTO)
            );

            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponseDTO<>(
                    true, 
                    registrarParticipanteMessage, 
                    null
                )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponseDTO<>(
                    false, 
                    e.getMessage(), 
                    null
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    false, 
                    e.getMessage(), 
                    null
                )
            );
        }
    } 

}
