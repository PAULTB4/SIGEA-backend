package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarParticipanteRequestDTO;

public class RegistrarParticipanteMapper {
    public static UsuarioDomainEntity requestToDomain(RegistrarParticipanteRequestDTO registrarParticipanteRequestDTO){
        UsuarioDomainEntity usuarioDomainEntityO = UsuarioDomainEntity.create(
            registrarParticipanteRequestDTO.getNombres(), 
            registrarParticipanteRequestDTO.getApellidos(), 
            registrarParticipanteRequestDTO.getCorreo(), 
            registrarParticipanteRequestDTO.getPassword(), 
            registrarParticipanteRequestDTO.getTelefono(), 
            registrarParticipanteRequestDTO.getExtensionTelefonica()
        );

        return usuarioDomainEntityO;
    }
}
