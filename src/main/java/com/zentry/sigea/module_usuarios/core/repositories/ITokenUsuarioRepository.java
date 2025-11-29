package com.zentry.sigea.module_usuarios.core.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;

public interface ITokenUsuarioRepository {
    // public Optional<TokenUsuarioDomainEntity> findByToken(String token);
    public void deleteByUsuario_Id(String usuario_id);
    public TokenUsuarioDomainEntity save(TokenUsuarioDomainEntity tokenUsuarioDomainEntity);
    public Optional<TokenUsuarioDomainEntity> findById(String id);
    public void deleteExpiredTokens(Instant now);
    public List<TokenUsuarioDomainEntity> findAll();
}