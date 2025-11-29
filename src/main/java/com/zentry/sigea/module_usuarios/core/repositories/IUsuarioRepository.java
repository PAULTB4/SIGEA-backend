package com.zentry.sigea.module_usuarios.core.repositories;

import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;

public interface IUsuarioRepository {
    public void save(UsuarioDomainEntity usuario);
    public Optional<UsuarioDomainEntity> findById(String id);
    public List<UsuarioDomainEntity> findAll();
    public Optional<UsuarioDomainEntity> findByCorreo(String correo);
    public String findIdByCorreo(String correo);
}
