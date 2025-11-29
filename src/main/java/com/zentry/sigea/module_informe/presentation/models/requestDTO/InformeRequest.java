package com.zentry.sigea.module_informe.presentation.models.requestDTO;

import java.time.LocalDate;

public class InformeRequest {
    private String actividadId;
    private String tipoInformeId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public InformeRequest() {}

    public InformeRequest(String actividadId, String tipoInformeId, LocalDate fechaInicio, LocalDate fechaFin) {
        this.actividadId = actividadId;
        this.tipoInformeId = tipoInformeId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getActividadId() {
        return actividadId;
    }

    public void setActividadId(String actividadId) {
        this.actividadId = actividadId;
    }

    public String getTipoInformeId() {
        return tipoInformeId;
    }

    public void setTipoInformeId(String tipoInformeId) {
        this.tipoInformeId = tipoInformeId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
