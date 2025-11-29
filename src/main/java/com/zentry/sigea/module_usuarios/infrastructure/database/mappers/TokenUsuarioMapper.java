package com.zentry.sigea.module_usuarios.infrastructure.database.mappers;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.TokenUsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

public class TokenUsuarioMapper {
    
    public static TokenUsuarioEntity toEntity(
        TokenUsuarioDomainEntity tokenUsuarioDomainEntity , 
        UsuarioEntity usuarioEntity
    ){
        TokenUsuarioEntity tokenUsuarioEntity = new TokenUsuarioEntity();

        tokenUsuarioEntity.setToken(tokenUsuarioDomainEntity.getToken());
        tokenUsuarioEntity.setUsuario(usuarioEntity);
        tokenUsuarioEntity.setExpiryDate(tokenUsuarioDomainEntity.getExpiryDate());

        return tokenUsuarioEntity;
    }

    public static TokenUsuarioDomainEntity toDomain(TokenUsuarioEntity tokenUsuarioEntity){
        TokenUsuarioDomainEntity tokenUsuarioDomainEntity = new TokenUsuarioDomainEntity();

        tokenUsuarioDomainEntity.setId(tokenUsuarioEntity.getId().toString());
        tokenUsuarioDomainEntity.setToken(tokenUsuarioEntity.getToken());
        tokenUsuarioDomainEntity.setUsuarioId(tokenUsuarioEntity.getUsuario().getId().toString());
        tokenUsuarioDomainEntity.setExpiryDate(tokenUsuarioEntity.getExpiryDate());

        return tokenUsuarioDomainEntity;
    }

}
