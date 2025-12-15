package com.zentry.sigea.module_usuarios.core.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;

public interface IUsuarioRolRepository {
    public List<RolDomainEntity> findRolesByUsuarioId(String usuarioId);
    public LocalDateTime findAsingandoEnByUsuarioIdAndRolId(String usuarioId , String rolId);
    public void save(String usuarioId , String rolId);
    public void saveOneUserWithAllRolesId(String usuarioId , List<String> listRolesId);
    public List<UsuarioDomainEntity> findAllUsuariosByNombreRol(String nombreRol);
    public void update(String usuarioId , String newRolId , String oldRolId);
}