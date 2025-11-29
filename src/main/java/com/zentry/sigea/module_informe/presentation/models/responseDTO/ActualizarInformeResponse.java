package com.zentry.sigea.module_informe.presentation.models.responseDTO;

import java.time.LocalDate;

public class ActualizarInformeResponse {
    private String id;
    private String actividadTitulo;
    private String tipoInformeEtiqueta;
    private LocalDate fechaSubida;
    private String archivoUrl;
    private String observaciones;

    public ActualizarInformeResponse() {}

    public ActualizarInformeResponse(
        String id,
        String actividadTitulo,
        String tipoInformeEtiqueta,
        LocalDate fechaSubida,
        String archivoUrl,
        String observaciones
    ) {
        this.id = id;
        this.actividadTitulo = actividadTitulo;
        this.tipoInformeEtiqueta = tipoInformeEtiqueta;
        this.fechaSubida = fechaSubida;
        this.archivoUrl = archivoUrl;
        this.observaciones = observaciones;
    }

    public String getId() {
        return id;
    }

    public String getActividadTitulo() {
        return actividadTitulo;
    }

    public String getTipoInformeEtiqueta() {
        return tipoInformeEtiqueta;
    }

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActividadTitulo(String actividadTitulo) {
        this.actividadTitulo = actividadTitulo;
    }

    public void setTipoInformeEtiqueta(String tipoInformeEtiqueta) {
        this.tipoInformeEtiqueta = tipoInformeEtiqueta;
    }

    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
