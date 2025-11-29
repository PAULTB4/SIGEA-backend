package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.zentry.sigea.module_usuarios.services.ParticipanteService;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.ITokenUsuarioRepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.TokenUsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.mappers.TokenUsuarioMapper;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.TokenUsuarioJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@Repository
public class TokenUsuarioRepositoryAdapter implements ITokenUsuarioRepository {

    private final ParticipanteService participanteService;
    
    private final TokenUsuarioJPARepository tokenUsuarioJPARepository;
    private final UsuarioJPARepository usuarioJPARepository;

    public TokenUsuarioRepositoryAdapter(
        TokenUsuarioJPARepository tokenUsuarioJPARepository , 
        UsuarioJPARepository usuarioJPARepository
    , ParticipanteService participanteService){
        this.tokenUsuarioJPARepository = tokenUsuarioJPARepository;
        this.usuarioJPARepository = usuarioJPARepository;
        this.participanteService = participanteService;
    }

    public Optional<TokenUsuarioDomainEntity> findById(String id){
        return tokenUsuarioJPARepository.findById(UUID.fromString(id))
            .map(tu -> TokenUsuarioMapper.toDomain(tu));
    }

    @Transactional
    public void deleteByUsuario_Id(String usuario_id){
        try {
            tokenUsuarioJPARepository.deleteByUsuario_Id(UUID.fromString(usuario_id));
            System.out.println("Token eliminado con exito");
        } catch (Exception e) {
            System.out.println("No se pudo eliminar el token");
            System.out.println(e.getMessage());
        }
    }

    public TokenUsuarioDomainEntity save(TokenUsuarioDomainEntity tokenUsuarioDomainEntity){
        UsuarioEntity usuarioEntity = usuarioJPARepository.findById(UUID.fromString(tokenUsuarioDomainEntity.getUsuarioId())).orElse(null);

        TokenUsuarioEntity tokenUsuarioEntity = tokenUsuarioJPARepository.save(
            TokenUsuarioMapper.toEntity(tokenUsuarioDomainEntity, usuarioEntity)
        );

        return TokenUsuarioMapper.toDomain(tokenUsuarioEntity);
    }

    public void deleteExpiredTokens(Instant now){
        tokenUsuarioJPARepository.deleteExpiredTokens(now);
    }

    public List<TokenUsuarioDomainEntity> findAll(){
        return tokenUsuarioJPARepository.findAll()
            .stream()
            .map(TokenUsuarioMapper::toDomain)
            .collect(Collectors.toList());
    }
}
