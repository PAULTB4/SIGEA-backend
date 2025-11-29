package com.zentry.sigea.module_informe.presentation.models.requestDTO;

import java.time.LocalDate;

public class ActualizarInformeRequest {
    private String archivoUrl;
    private String observaciones;
    private LocalDate fechaSubida;

    public ActualizarInformeRequest() {}

    public ActualizarInformeRequest(String archivoUrl, String observaciones, LocalDate fechaSubida) {
        this.archivoUrl = archivoUrl;
        this.observaciones = observaciones;
        this.fechaSubida = fechaSubida;
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
}
