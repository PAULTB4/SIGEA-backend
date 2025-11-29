package com.zentry.sigea.module_usuarios.core.entities;

import java.time.LocalDateTime;

public class RolDomainEntity {
    private String nombreRol;
    private String descripcion;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public String getNombreRol() {
        return nombreRol;
    }
    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public static RolDomainEntity create(
        String nombreRol , 
        String descripcion
    ){
        LocalDateTime nowLocalDateTime = LocalDateTime.now();

        RolDomainEntity rolDomainEntity = new RolDomainEntity();

        rolDomainEntity.setNombreRol(nombreRol);
        rolDomainEntity.setDescripcion(descripcion);
        rolDomainEntity.setCreateAt(nowLocalDateTime);
        rolDomainEntity.setUpdateAt(nowLocalDateTime);

        return rolDomainEntity;
    }
}
