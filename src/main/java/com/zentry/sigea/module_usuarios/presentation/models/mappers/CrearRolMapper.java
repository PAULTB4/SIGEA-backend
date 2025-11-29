package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.CrearRolRequestDTO;

public class CrearRolMapper {
    public static RolDomainEntity requestToDomain(CrearRolRequestDTO crearRolRequestDTO){
        RolDomainEntity rolDomainEntity = RolDomainEntity.create(
            crearRolRequestDTO.getNombreRol(), 
            crearRolRequestDTO.getDescripcion()
        );

        return rolDomainEntity;
    }
}
