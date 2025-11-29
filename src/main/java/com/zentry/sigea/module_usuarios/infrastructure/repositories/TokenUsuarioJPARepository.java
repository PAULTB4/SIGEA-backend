package com.zentry.sigea.module_usuarios.infrastructure.repositories;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_usuarios.infrastructure.database.entities.TokenUsuarioEntity;

public interface TokenUsuarioJPARepository extends JpaRepository<TokenUsuarioEntity , UUID>{
    Optional<TokenUsuarioEntity> findByToken(String token);
    public Optional<TokenUsuarioEntity> findById(UUID id);
    void deleteByUsuario_Id(UUID usuario_id);
    TokenUsuarioEntity save(TokenUsuarioEntity tokenUsuarioEntity);

    @Modifying
    @Transactional
    @Query(
        """
            DELETE FROM TokenUsuarioEntity t WHERE t.expiryDate = :now
        """
    )
    public void deleteExpiredTokens(@Param("now") Instant now);
}
