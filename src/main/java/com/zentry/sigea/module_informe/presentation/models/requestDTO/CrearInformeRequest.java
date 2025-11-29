package com.zentry.sigea.module_informe.presentation.models.requestDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para recibir datos de creación de informe desde el frontend
 * Recibe solo los IDs de actividad y tipo de informe, y los datos principales
 */
public class CrearInformeRequest {
    @NotBlank(message="El ID de la actividad no puede estar vacío")
    private String actividadId;
    @NotNull(message="El ID del tipo de informe no puede estar vacío")
    private String tipoInformeId;

    private LocalDate fechaSubida;
    private String observaciones;

    // Constructor vacío para Jackson
    public CrearInformeRequest() {}

    public CrearInformeRequest(String actividadId, String tipoInformeId, LocalDate fechaSubida, String observaciones) {
        this.actividadId = actividadId;
        this.tipoInformeId = tipoInformeId;
        this.fechaSubida = fechaSubida;
        this.observaciones = observaciones;
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

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }
    public void setFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}