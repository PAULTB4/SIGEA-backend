package com.zentry.sigea.module_asistencias.presentation.models.responseDTO;

import java.time.LocalDateTime;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;

public class AsistenciaResponse {
    private String id;
    private String sesionId;
    private String inscripcionId;
    private Boolean presente;
    private LocalDateTime registradoEn;

    public AsistenciaResponse() {}

    public AsistenciaResponse(String id, String sesionId, String inscripcionId,
                            Boolean presente, LocalDateTime registradoEn) {
        this.id = id;
        this.sesionId = sesionId;
        this.inscripcionId = inscripcionId;
        this.presente = presente;
        this.registradoEn = registradoEn;
    }

    public static AsistenciaResponse fromDomain(AsistenciaDomainEntity domain, String id) {
        return new AsistenciaResponse(
            id,
            domain.getSesionId(),
            domain.getInscripcionId(),
            domain.getPresente(),
            domain.getRegistradoEn()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getRegistradoEn() {
        return registradoEn;
    }

    public void setRegistradoEn(LocalDateTime registradoEn) {
        this.registradoEn = registradoEn;
    }
}
