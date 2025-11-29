package com.zentry.sigea.module_usuarios.services.serviceDTO;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarAsistenciaItemServiceDTO {
    private String inscripcionId;
    private Boolean presente;
    private LocalDateTime registradoEn;

    public RegistrarAsistenciaItemServiceDTO(
        String inscripcionId,
        Boolean presente, 
        Optional<LocalDateTime> registradoEn
    ){
        this.inscripcionId = inscripcionId;
        this.presente = presente;
        this.registradoEn = registradoEn.orElse(null);
    }
}
