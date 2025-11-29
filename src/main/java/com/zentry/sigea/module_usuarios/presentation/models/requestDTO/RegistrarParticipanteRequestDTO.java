package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegistrarParticipanteRequestDTO {
    
    @NotBlank(message = "Debe especificar su nombre completo.")
    private String nombres;

    @NotBlank(message = "Debe especificar su apellido completo.")
    private String apellidos;
    
    @NotBlank(message = "Debe especificar una direccion de correo.")
    private String correo;
    
    @NotBlank(message = "Debe colocar una contraseña.")
    private String password;
    
    @NotBlank(message = "Debe especificar un numero de telefono.")
    @Size(max = 25 , message = "El numero de telefono no debe tener mas de 25 caracteres.")
    private String telefono;
    
    @NotBlank(message = "Debe especificar la extension telefonica.")
    @Size(max = 25 , message = "La extension telefonica no debe tener mas de 8 caracteres.")
    private String extensionTelefonica;
}