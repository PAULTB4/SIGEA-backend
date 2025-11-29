package com.zentry.sigea.module_usuarios.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;

public interface IRolRepository {
    public void save(RolDomainEntity rolDomainEntity);
    public List<RolDomainEntity> findAll();
    public Optional<RolDomainEntity> findById(String id);
    public Optional<RolDomainEntity> findByNombreRol(String nombreRol);
    public String findIdByNombreRol(String nombreRol);
}
