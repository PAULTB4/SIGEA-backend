package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IRolRepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.mappers.RolMapper;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;

@Repository
public class RolRepositoryAdapter implements IRolRepository{
    
    private final RolJPARepository rolJPARepository;

    public RolRepositoryAdapter(RolJPARepository rolJPARepository){
        this.rolJPARepository = rolJPARepository;
    }

    public void save(RolDomainEntity rolDomainEntity){
        rolJPARepository.save(
            RolMapper.toEntity(rolDomainEntity)
        );
    }

    public List<RolDomainEntity> findAll(){
        return rolJPARepository.findAll()
            .stream()
            .map(RolMapper::toDomain)
            .collect(Collectors.toList());
    }

    public Optional<RolDomainEntity> findById(String id){
        return rolJPARepository.findById(UUID.fromString(id))
            .map(r -> RolMapper.toDomain(r));
    }

    public Optional<RolDomainEntity> findByNombreRol(String nombreRol){
        return rolJPARepository.findByNombreRol(nombreRol)
            .map(r -> RolMapper.toDomain(r));
    }

    public String findIdByNombreRol(String nombreRol){
        return rolJPARepository.findIdByNombreRol(nombreRol)
            .map(id -> id.toString())
            .orElse(null);
    }
}
