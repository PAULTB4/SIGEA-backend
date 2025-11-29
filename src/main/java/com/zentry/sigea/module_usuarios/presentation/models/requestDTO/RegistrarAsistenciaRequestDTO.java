package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegistrarAsistenciaRequestDTO {

    @NotNull(message = "Debe proporcionar una sesion.")
    private String sesionId;

    @NotEmpty(message = "Debe enviar la lista de presentes, junato a su ID de insripcion.")
    private List<RegistrarAsistenciaItemRequestDTO> registrarAsistenciaItemRequestDTOs;
}