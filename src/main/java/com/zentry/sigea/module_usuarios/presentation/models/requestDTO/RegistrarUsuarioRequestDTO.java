package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrarUsuarioRequestDTO {
    
    @NotNull(message = "Debe proporcionar sus nombres.")
    @Size(message = "Sus nombres no deben tener más de 100 caracteres.")
    private String nombres;
    
    @NotNull(message = "Debe proporcionar sus apellidos.")
    @Size(message = "Sus apellidos no deben tener más de 100 caracteres.")
    private String apellidos;

    @NotNull(message = "Debe proporcionar un correo.")
    @Email(message = "Debe escribir un correo valido.")
    @Size(max = 100 , message = "El correo no debe tener más de 100 caracteres.")
    private String correo;
    
    @NotNull(message = "Debe colocar una contraseña.")
    @Size(min = 8 , max = 100 , message = "La contraseña debe tener entre 8 y 100 caracteres de longitud.")
    private String password;

    @NotNull(message = "Debe proporcionar un numero de telefono.")
    @Size(min = 7 , max = 25 , message = "El numero de telefono debe tener entre 7 y 25 digitos.")
    private String telefono;

    @NotNull(message = "Debe proporcionar una extension telefonica.")
    @Size(min = 2 , max = 8 , message = "La extension telefonica debe tener entre 2 y 8 caracteres.")
    private String extensionTelefonica;

    @NotEmpty(message = "Debe proporcionar al menos un rol al usuario.")
    private List<String> rolId;

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExtensionTelefonica() {
        return extensionTelefonica;
    }
    public void setExtensionTelefonica(String extensionTelefonica) {
        this.extensionTelefonica = extensionTelefonica;
    }

    public List<String> getRolId() {
        return rolId;
    }
    public void setRolId(List<String> rolId) {
        this.rolId = rolId;
    }
}
