package com.zentry.sigea.module_informe.presentation.models.responseDTO;

import java.time.LocalDate;
import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;

/**
 * DTO para enviar datos de informe al frontend
 */
public class InformeResponse {
    private String id;
    private String actividadTitulo;
    private String tipoInformeEtiqueta;
    private LocalDate fechaSubida;
    private String archivoUrl;
    private String observaciones;

    // Constructor vacío para Jackson
    public InformeResponse() {}

    public InformeResponse(
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

    /**
     * Factory method para crear un InformeResponse desde una entidad de dominio
     */
    public static InformeResponse fromEntity(InformeDomainEntity informeDomainEntity) {
        return new InformeResponse(
            informeDomainEntity.getId(),
            informeDomainEntity.getActividadTitulo(),
            informeDomainEntity.getTipoInformeEtiqueta(),
            informeDomainEntity.getFechaSubida(),
            informeDomainEntity.getArchivoUrl(),
            informeDomainEntity.getObservaciones()
        );
    }

    // Getters y setters

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDate getFechaSubida() {
        return fechaSubida;
    }
    public void setFechaSubida(LocalDate fechaSubida) {
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
}
