package com.zentry.sigea.module_asistencias.presentation.models.requestDTO;

import jakarta.validation.constraints.NotNull;

public class AsistenciaRequest {
    
    @NotNull(message = "Debe especificar el nuevo estado de presencia")
    private Boolean presente;

    public AsistenciaRequest() {}

    public AsistenciaRequest(Boolean presente) {
        this.presente = presente;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
}
