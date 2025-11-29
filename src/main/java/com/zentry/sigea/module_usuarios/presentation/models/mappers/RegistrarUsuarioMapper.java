package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarUsuarioRequestDTO;

public class RegistrarUsuarioMapper {
    public static UsuarioDomainEntity requestToDomain(RegistrarUsuarioRequestDTO registrarUsuarioRequestDTO){
        UsuarioDomainEntity usuarioDomainEntity = UsuarioDomainEntity.create(
            registrarUsuarioRequestDTO.getNombres(), 
            registrarUsuarioRequestDTO.getApellidos(), 
            registrarUsuarioRequestDTO.getCorreo(), 
            registrarUsuarioRequestDTO.getPassword(), 
            registrarUsuarioRequestDTO.getTelefono(), 
            registrarUsuarioRequestDTO.getExtensionTelefonica()
        );
        
        return usuarioDomainEntity;
    }
}
