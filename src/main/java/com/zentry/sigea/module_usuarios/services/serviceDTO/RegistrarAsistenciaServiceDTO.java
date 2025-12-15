package com.zentry.sigea.module_usuarios.services.serviceDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarAsistenciaServiceDTO {
    private String sesionId;
    private List<RegistrarAsistenciaItemServiceDTO> registrarAsistenciaItemServiceDTOs = new ArrayList<>();
}