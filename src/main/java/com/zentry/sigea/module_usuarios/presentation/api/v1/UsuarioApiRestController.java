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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zentry.sigea.module_informe.config.InformeMappingConfig;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.LoginUsuarioRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.GeneralResponseDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.LoginResponseDTO;
import com.zentry.sigea.module_usuarios.services.UsuarioService;
import com.zentry.sigea.security.UsuarioAuthInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioApiRestController {

    private final InformeMappingConfig informeMappingConfig;
    private final UsuarioService usuarioService;

    public UsuarioApiRestController(
        UsuarioService usuarioService, 
        InformeMappingConfig informeMappingConfig
    ){
        this.usuarioService = usuarioService;
        this.informeMappingConfig = informeMappingConfig;
    }    

    @PostMapping("/auth/login")
    @Operation(
        summary = "Inicio de sesion de los usuarios" , 
        tags = {"Login"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> loginUsuario(
        @Valid @RequestBody LoginUsuarioRequestDTO loginUsuarioRequestDTO ,
        BindingResult bindingResult , 
        HttpServletResponse response
    ) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new GeneralResponseDTO<>(false, "Ya estás autenticado", null)
            );
        }

        if (bindingResult.hasErrors()) {
            Map<String , String> errorMessages = new HashMap<>();

            bindingResult.getFieldErrors().forEach((errorField) -> {
                errorMessages.put(errorField.getField(), errorField.getDefaultMessage());
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponseDTO<>(
                    false, 
                    "Error al iniciar sesion.", 
                    errorMessages
                )
            );
        }

        try {
            LoginResponseDTO loginResponseDTO = usuarioService.login(
                loginUsuarioRequestDTO.getCorreo(), 
                loginUsuarioRequestDTO.getPassword(), 
                loginUsuarioRequestDTO.getRememberMe()
            );

            String refreshToken = loginResponseDTO.getRefreshToken();
            String idRefreshToken = loginResponseDTO.getIdRefreshToken();
            
            Cookie cookieRefreshToken = new Cookie("refreshToken", refreshToken);
            cookieRefreshToken.setHttpOnly(true);
            cookieRefreshToken.setSecure(true);
            cookieRefreshToken.setPath("/");

            Cookie cookieIdRefreshToken = new Cookie("idRefreshToken", idRefreshToken);
            cookieIdRefreshToken.setHttpOnly(true);
            cookieIdRefreshToken.setSecure(true);
            cookieIdRefreshToken.setPath("/");
            
            if(loginUsuarioRequestDTO.getRememberMe()){
                cookieIdRefreshToken.setMaxAge(60* 60 * 24 * 1);
                cookieRefreshToken.setMaxAge(60* 60 * 24 * 1);
            } else {
                cookieIdRefreshToken.setMaxAge(60 * 60 * 1);
                cookieRefreshToken.setMaxAge(60 * 60 * 1);
            }

            response.addCookie(cookieRefreshToken);
            response.addCookie(cookieIdRefreshToken);
            
            return ResponseEntity.status(HttpStatus.OK).body(
                new GeneralResponseDTO<Map<String , ?>>(
                    true , 
                    "Inicio de sesion exitoso", 
                    Map.of(
                        "accessToken" , loginResponseDTO.getAccessToken() , 
                        "emailVerificado" , loginResponseDTO.getCorreoVerificado()
                    )
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
                    "Ocurrio un error al procesar la solicitud.", 
                    null
                )
            );
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR' , 'ROLE_ORGANIZADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Cerrar sesion",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Logout"}
    )
    public ResponseEntity<GeneralResponseDTO<?>> logoutUsuario(
        @AuthenticationPrincipal UsuarioAuthInfo usuarioAuthInfo , 
        @RequestHeader("Authorization") String authHeader ,
        HttpServletResponse response
    ){
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GeneralResponseDTO<>(false, "No Authorization header", null));
            }

            String accessToken = authHeader.substring(7);
            String messageLogout = usuarioService.logout(usuarioAuthInfo.getId() , accessToken);
            
            Cookie cookieRefreshToken = new Cookie("refreshToken", null);
            cookieRefreshToken.setHttpOnly(true);
            cookieRefreshToken.setSecure(true);
            cookieRefreshToken.setPath("/");
            cookieRefreshToken.setMaxAge(0); // Expira inmediatamente

            Cookie cookieIdRefreshToken = new Cookie("idRefreshToken", null);
            cookieIdRefreshToken.setHttpOnly(true);
            cookieIdRefreshToken.setSecure(true);
            cookieIdRefreshToken.setPath("/");
            cookieIdRefreshToken.setMaxAge(0);

            response.addCookie(cookieRefreshToken);
            response.addCookie(cookieIdRefreshToken);

            return ResponseEntity.status(HttpStatus.OK).body(
                new GeneralResponseDTO<>(
                    true, 
                    messageLogout, 
                    null
                )
            );

        } catch (RuntimeException r){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    true, 
                    r.getMessage(), 
                    null
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    true, 
                    e.getMessage(), 
                    null
                )
            );
        }
    }

    @DeleteMapping("/eliminar-usuario/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR' , 'ROLE_ORGANIZADOR' , 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Eliminar un usuario por su id",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        },
        tags = {"Eliminar"}
    )
    public ResponseEntity<GeneralResponseDTO<String>> eliminarUsuario(
        @PathVariable("id") String usuarioId
    ){
        try {
            String responseMessage = usuarioService.eliminarUsuario(usuarioId);

            return ResponseEntity.status(HttpStatus.OK).body(
                new GeneralResponseDTO<>(
                    true, 
                    responseMessage, 
                    null
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralResponseDTO<>(
                    true, 
                    e.getMessage(), 
                    null
                )
            );
        }
    }
}
