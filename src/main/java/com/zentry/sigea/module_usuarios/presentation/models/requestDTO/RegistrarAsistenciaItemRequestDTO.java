package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegistrarAsistenciaItemRequestDTO {
    @NotBlank(message = "Debe especificar el ID de incripcion")
    private String inscripcionId;

    private Boolean presente;
    
    private LocalDateTime registradoEn;
}
