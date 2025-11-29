package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioRolEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.mappers.RolMapper;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioRolJPARepository;

@Repository
public class UsuarioRolRepositoryAdapter implements IUsuarioRolRepository {
    
    private final UsuarioRolJPARepository usuarioRolJPARepository;
    
    private final UsuarioJPARepository usuarioJPARepository;
    private final RolJPARepository rolJPARepository;


    public UsuarioRolRepositoryAdapter(
        UsuarioRolJPARepository usuarioRolJPARepository , 
        UsuarioJPARepository usuarioJPARepository , 
        RolJPARepository rolJPARepository
    ){
        this.usuarioRolJPARepository = usuarioRolJPARepository;
        this.usuarioJPARepository = usuarioJPARepository;
        this.rolJPARepository = rolJPARepository;
    }

    public List<RolDomainEntity> findRolesByUsuarioId(String usuarioId){
        List<UsuarioRolEntity> usuarioRolEntity = usuarioRolJPARepository.findById_IdUsuario(UUID.fromString(usuarioId));
        
        return usuarioRolEntity.stream()
            .map(ur -> RolMapper.toDomain(ur.getRol()))
            .collect(Collectors.toList());
    }

    public LocalDateTime findAsingandoEnByUsuarioIdAndRolId(String usuarioId , String rolId){
        return usuarioRolJPARepository.findAsignadoEnById_IdUsuarioAndId_IdRol(
            UUID.fromString(usuarioId) , 
            UUID.fromString(rolId)
        );
    }

    public void save(String usuarioId , String rolId){
        LocalDateTime nowLocalDateTime = LocalDateTime.now();

        UsuarioRolEntity usuarioRolEntity = new UsuarioRolEntity();

        usuarioRolEntity.setRol(
            rolJPARepository.findById(UUID.fromString(rolId)).orElseThrow(
                () -> new RuntimeException("No se encontro el ID de uno de los roles.")
            )
        );

        usuarioRolEntity.setUsuario(
            usuarioJPARepository.findById(UUID.fromString(usuarioId)).orElseThrow(
                () -> new RuntimeException("No se encontro el ID del usuario.")
            )
        );

        usuarioRolEntity.setAsignadoEn(nowLocalDateTime);

        usuarioRolJPARepository.save(usuarioRolEntity);
    }

    public void saveOneUserWithAllRolesId(String usuarioId , List<String> listRolesId){
        List<UsuarioRolEntity> listUsuarioRolEntities = new ArrayList<>();
        LocalDateTime nowLocalDateTime = LocalDateTime.now();

        for(String rolId : listRolesId){
            UsuarioRolEntity usuarioRolEntity = new UsuarioRolEntity();

            usuarioRolEntity.setRol(
                rolJPARepository.findById(UUID.fromString(rolId)).orElseThrow(
                    () -> new RuntimeException("No se encontro el ID de uno de los roles.")
                )
            );
                    
            usuarioRolEntity.setUsuario(
                usuarioJPARepository.findById(UUID.fromString(usuarioId)).orElseThrow(
                    () -> new RuntimeException("No se encontro el ID del usuario.")
                )
            );

            usuarioRolEntity.setAsignadoEn(nowLocalDateTime);

            listUsuarioRolEntities.add(usuarioRolEntity);
        }

        usuarioRolJPARepository.saveAll(listUsuarioRolEntities);
    }
}
