package com.zentry.sigea.module_usuarios.services.serviceDTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListarUsuariosServiceDTO {
    private String id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String dni;
    private Boolean correoVerificado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private String telefono;
    private String extensionTelefonica;

    private List<String> nombresRoles;
}
