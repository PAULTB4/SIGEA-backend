package com.zentry.sigea.module_usuarios.services.serviceDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardParticipanteAsistenciasServiceDTO {
    private String actividadId;
    private String tituloActividad;
    private LocalDate fechaInicioActividad;
    private LocalDate fechaFinActividad;
    private String modalidadActividad;
    private LocalDateTime updateAtActividad;

    private Integer totalInscritosActividad;
    private Integer asistentesUltimaSesion;
    private Double tasaAsistenciasUltimaSesion;

    private String sesionId;

    private List<DashboardParticipanteAsistenciasItemServiceDTO> listParticipantesInfo = new ArrayList<>();
}
