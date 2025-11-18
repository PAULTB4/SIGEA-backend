package com.zentry.sigea.module_asistencias.presentation.models.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegistrarAsistenciaRequest {
    
    @NotBlank(message = "El ID de sesión es obligatorio")
    private String sesionId;
    
    @NotBlank(message = "El ID de inscripción es obligatorio")
    private String inscripcionId;
    
    @NotNull(message = "Debe especificar si está presente")
    private Boolean presente;

    public RegistrarAsistenciaRequest() {}

    public RegistrarAsistenciaRequest(String sesionId, String inscripcionId, Boolean presente) {
        this.sesionId = sesionId;
        this.inscripcionId = inscripcionId;
        this.presente = presente;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public String getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(String inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
}