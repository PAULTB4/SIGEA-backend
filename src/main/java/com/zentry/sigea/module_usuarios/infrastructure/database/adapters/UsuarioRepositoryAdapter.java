package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.mappers.UsuarioMapper;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@Repository
public class UsuarioRepositoryAdapter implements IUsuarioRepository {
    
    private final UsuarioJPARepository usuarioJPARepository;

    public UsuarioRepositoryAdapter(UsuarioJPARepository usuarioJPARepository){
        this.usuarioJPARepository = usuarioJPARepository;
    }

    public void save(UsuarioDomainEntity usuario){
        usuarioJPARepository.saveAndFlush(
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

    public long countAllUsers(){
        return usuarioJPARepository.count();
    }

    public void update(UsuarioDomainEntity usuarioDomainEntity , Boolean passwordMatches){
        UsuarioEntity usuarioEntity = usuarioJPARepository.findById(UUID.fromString(usuarioDomainEntity.getId()))
            .orElseThrow(
                () -> new RuntimeException("No se encontro al usuario con el id especificado")
            );
        
        usuarioEntity.setNombres(usuarioDomainEntity.getNombres());
        usuarioEntity.setApellidos(usuarioDomainEntity.getApellidos());
        usuarioEntity.setCorreo(usuarioDomainEntity.getCorreo());
        
        if(!passwordMatches){
            usuarioEntity.setPasswordHash(usuarioDomainEntity.getPasswordHash());
        }

        usuarioEntity.setDni(usuarioDomainEntity.getDni());
        usuarioEntity.setCorreoVerificado(usuarioDomainEntity.getCorreoVerificado());

        LocalDateTime nowDateTime = LocalDateTime.now();

        usuarioEntity.setUpdatedAt(nowDateTime);

        usuarioJPARepository.save(usuarioEntity);
    }

    public String findPasswordHashById(String id){
        return usuarioJPARepository.findPasswordHashById(UUID.fromString(id));
    }

    public void deleteById(String id){
        usuarioJPARepository.deleteById(UUID.fromString(id));
    }
}
