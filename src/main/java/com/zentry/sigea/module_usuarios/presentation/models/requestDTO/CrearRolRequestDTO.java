package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CrearRolRequestDTO {
    @NotBlank(message = "Debe proporcionar el nombre del rol.")
    private String nombreRol;

    private String descripcion;
}
