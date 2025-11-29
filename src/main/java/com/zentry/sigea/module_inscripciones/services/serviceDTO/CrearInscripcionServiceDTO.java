package com.zentry.sigea.module_inscripciones.services.serviceDTO;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearInscripcionServiceDTO {
    private LocalDate fechaInscripcion;
    private String usuarioId;
    private String actividadId;
    private String estadoId;
}
