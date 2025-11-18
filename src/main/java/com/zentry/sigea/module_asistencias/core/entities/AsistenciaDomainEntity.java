package com.zentry.sigea.module_asistencias.core.entities;

import java.time.LocalDateTime;

public class AsistenciaDomainEntity {
    private String id;
    private String sesionId;
    private String inscripcionId;
    private Boolean presente;
    private LocalDateTime registradoEn;

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

    public static AsistenciaDomainEntity create(
        String sesionId,
        String inscripcionId,
        Boolean presente
    ){
        LocalDateTime nowLocalDateTime = LocalDateTime.now();

        AsistenciaDomainEntity asistenciaDomainEntity = new AsistenciaDomainEntity();

        asistenciaDomainEntity.setSesionId(sesionId);
        asistenciaDomainEntity.setInscripcionId(inscripcionId);
        asistenciaDomainEntity.setPresente(presente != null ? presente : false);
        asistenciaDomainEntity.setRegistradoEn(nowLocalDateTime);

        return asistenciaDomainEntity;
    }

    public static AsistenciaDomainEntity reconstruct(
        String id,
        String sesionId,
        String inscripcionId,
        Boolean presente,
        LocalDateTime registradoEn
    ) {
        AsistenciaDomainEntity asistencia = new AsistenciaDomainEntity();
        asistencia.setId(id);
        asistencia.setSesionId(sesionId);
        asistencia.setInscripcionId(inscripcionId);
        asistencia.setPresente(presente);
        asistencia.setRegistradoEn(registradoEn);
        return asistencia;
    }

    public void marcarPresente() {
        this.presente = true;
    }

    public void marcarAusente() {
        this.presente = false;
    }

    public boolean estaPresente() {
        return presente != null && presente;
    }
}