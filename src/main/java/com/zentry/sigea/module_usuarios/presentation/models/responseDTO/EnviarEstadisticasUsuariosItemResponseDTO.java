package com.zentry.sigea.module_usuarios.presentation.models.responseDTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnviarEstadisticasUsuariosItemResponseDTO {
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
}
