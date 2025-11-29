package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUsuarioRequestDTO {
    
    @NotBlank(message = "Debe ingresar un correo.")
    @Email(message = "Debe escribir un correo valido.")
    @Size(max = 100 , message = "El correo no debe tener más de 100 caracteres.")
    private String correo;
    
    @NotBlank(message = "Debe ingresar la contraseña.")
    @Size(min = 6 , max = 100 , message = "La contraseña debe tener entre 6 y 100 caracteres de longitud.")
    private String password;

    @Schema(description = "Indica si se debe recordar la sesión", defaultValue = "false")
    private Boolean rememberMe = Boolean.FALSE;
}
