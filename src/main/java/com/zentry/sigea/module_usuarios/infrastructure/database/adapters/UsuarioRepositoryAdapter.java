package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.mappers.UsuarioMapper;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@Repository
public class UsuarioRepositoryAdapter implements IUsuarioRepository {
    
    private final UsuarioJPARepository usuarioJPARepository;

    public UsuarioRepositoryAdapter(UsuarioJPARepository usuarioJPARepository){
        this.usuarioJPARepository = usuarioJPARepository;
    }

    public void save(UsuarioDomainEntity usuario){
        usuarioJPARepository.save(
            UsuarioMapper.toEntity(usuario)
        );
    }

    public Optional<UsuarioDomainEntity> findById(String id){
        return usuarioJPARepository.findById(UUID.fromString(id))
            .map(u -> UsuarioMapper.toDomain(u));
    }

    public List<UsuarioDomainEntity> findAll(){
        return usuarioJPARepository.findAll()
            .stream()
            .map(UsuarioMapper::toDomain)
            .collect(Collectors.toList());
    }

    public Optional<UsuarioDomainEntity> findByCorreo(String correo){
        return usuarioJPARepository.findByCorreo(correo)
            .map(u -> UsuarioMapper.toDomain(u));
    }

    public String findIdByCorreo(String correo){
        return usuarioJPARepository.findIdByCorreo(correo)
            .map(id -> id.toString())
            .orElse(null);
    }
}
