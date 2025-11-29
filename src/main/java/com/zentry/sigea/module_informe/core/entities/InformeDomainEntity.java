package com.zentry.sigea.module_informe.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InformeDomainEntity {
    private String id;
    private String actividadId;
    private String tipoInformeId;
    private LocalDate fechaSubida;
    private String archivoUrl;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String actividadTitulo;
    private String tipoInformeEtiqueta;

    // Constructor vacío
    public InformeDomainEntity() {}

    // Constructor completo
    public InformeDomainEntity(
        String id,
        String actividadId,
        String tipoInformeId,
        LocalDate fechaSubida,
        String archivoUrl,
        String observaciones,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String actividadTitulo,
        String tipoInformeEtiqueta
    ) {
        this.id = id;
        this.actividadId = actividadId;
        this.tipoInformeId = tipoInformeId;
        this.fechaSubida = fechaSubida;
        this.archivoUrl = archivoUrl;
        this.observaciones = observaciones;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.actividadTitulo = actividadTitulo;
        this.tipoInformeEtiqueta = tipoInformeEtiqueta;
    }

    // Factory method para crear un informe nuevo
    public static InformeDomainEntity create(
        String actividadId,
        String tipoInformeId,
        LocalDate fechaSubida,
        String archivoUrl,
        String observaciones
    ) {
        LocalDateTime now = LocalDateTime.now();
        return new InformeDomainEntity(
            null,
            actividadId,
            tipoInformeId,
            fechaSubida,
            archivoUrl,
            observaciones,
            now,
            now,
            null,
            null
        );
    }

    // Métodos de negocio enriquecidos
    public void actualizarArchivo(String archivoUrl) {
        this.archivoUrl = archivoUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void actualizarObservaciones(String observaciones) {
        this.observaciones = observaciones;
        this.updatedAt = LocalDateTime.now();
    }

    public void actualizarFechaSubida(LocalDate fechaSubida) {
        this.fechaSubida = fechaSubida;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean esReciente() {
        return fechaSubida != null && fechaSubida.isAfter(LocalDate.now().minusDays(7));
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public String getActividadId() {
        return actividadId;
    }

    public String getTipoInformeId() {
        return tipoInformeId;
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

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }
    public void setFechaSubida(LocalDate fechaSubida) { this.fechaSubida = fechaSubida; }

    // Getters y setters para los nuevos campos
    public String getActividadTitulo() {
        return actividadTitulo;
    }

    public void setActividadTitulo(String actividadTitulo) {
        this.actividadTitulo = actividadTitulo;
    }

    public String getTipoInformeEtiqueta() {
        return tipoInformeEtiqueta;
    }

    public void setTipoInformeEtiqueta(String tipoInformeEtiqueta) {
        this.tipoInformeEtiqueta = tipoInformeEtiqueta;
    }
}
