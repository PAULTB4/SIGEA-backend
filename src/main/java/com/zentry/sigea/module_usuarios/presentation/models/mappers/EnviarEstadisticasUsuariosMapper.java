package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import java.util.ArrayList;
import java.util.List;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.EnviarEstadisticasUsuariosItemResponseDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.EnviarEstadisticasUsuariosResponseDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.EnviarEstadisticasUsuariosServiceDTO;

public class EnviarEstadisticasUsuariosMapper {
    public static EnviarEstadisticasUsuariosResponseDTO serviceToResponse(EnviarEstadisticasUsuariosServiceDTO enviarEstadisticasUsuariosServiceDTO){
        EnviarEstadisticasUsuariosResponseDTO enviarEstadisticasUsuariosResponseDTO = new EnviarEstadisticasUsuariosResponseDTO();

        enviarEstadisticasUsuariosResponseDTO.setTotalRegisteredUsers(enviarEstadisticasUsuariosServiceDTO.getTotalRegisteredUsers());
        
        List<EnviarEstadisticasUsuariosItemResponseDTO> listUsuarios = new ArrayList<>();
        for(UsuarioDomainEntity usuario : enviarEstadisticasUsuariosServiceDTO.getListUsuarioDomainEntities()){
            EnviarEstadisticasUsuariosItemResponseDTO enviarEstadisticasUsuariosItemResponseDTO = new EnviarEstadisticasUsuariosItemResponseDTO();

            enviarEstadisticasUsuariosItemResponseDTO.setId(usuario.getId());
            enviarEstadisticasUsuariosItemResponseDTO.setNombres(usuario.getNombres());
            enviarEstadisticasUsuariosItemResponseDTO.setApellidos(usuario.getApellidos());
            enviarEstadisticasUsuariosItemResponseDTO.setCorreo(usuario.getCorreo());
            enviarEstadisticasUsuariosItemResponseDTO.setDni(usuario.getDni());
            enviarEstadisticasUsuariosItemResponseDTO.setCorreoVerificado(usuario.getCorreoVerificado());
            enviarEstadisticasUsuariosItemResponseDTO.setCreatedAt(usuario.getCreatedAt());
            enviarEstadisticasUsuariosItemResponseDTO.setUpdatedAt(usuario.getUpdatedAt());
            enviarEstadisticasUsuariosItemResponseDTO.setTelefono(usuario.getTelefono());
            enviarEstadisticasUsuariosItemResponseDTO.setExtensionTelefonica(usuario.getExtensionTelefonica());

            listUsuarios.add(enviarEstadisticasUsuariosItemResponseDTO);
        }
        enviarEstadisticasUsuariosResponseDTO.setListUsuarios(listUsuarios);

        enviarEstadisticasUsuariosResponseDTO.setTotalUsuariosOrganizador(enviarEstadisticasUsuariosServiceDTO.getTotalUsuariosOrganizador());
        List<EnviarEstadisticasUsuariosItemResponseDTO> listUsuariosOrganizador = new ArrayList<>();
        for(UsuarioDomainEntity usuarioOrganizador : enviarEstadisticasUsuariosServiceDTO.getListUsuariosOrganizador()){
            EnviarEstadisticasUsuariosItemResponseDTO enviarEstadisticasUsuariosItemResponseDTO = new EnviarEstadisticasUsuariosItemResponseDTO();

            enviarEstadisticasUsuariosItemResponseDTO.setId(usuarioOrganizador.getId());
            enviarEstadisticasUsuariosItemResponseDTO.setNombres(usuarioOrganizador.getNombres());
            enviarEstadisticasUsuariosItemResponseDTO.setApellidos(usuarioOrganizador.getApellidos());
            enviarEstadisticasUsuariosItemResponseDTO.setCorreo(usuarioOrganizador.getCorreo());
            enviarEstadisticasUsuariosItemResponseDTO.setDni(usuarioOrganizador.getDni());
            enviarEstadisticasUsuariosItemResponseDTO.setCorreoVerificado(usuarioOrganizador.getCorreoVerificado());
            enviarEstadisticasUsuariosItemResponseDTO.setCreatedAt(usuarioOrganizador.getCreatedAt());
            enviarEstadisticasUsuariosItemResponseDTO.setUpdatedAt(usuarioOrganizador.getUpdatedAt());
            enviarEstadisticasUsuariosItemResponseDTO.setTelefono(usuarioOrganizador.getTelefono());
            enviarEstadisticasUsuariosItemResponseDTO.setExtensionTelefonica(usuarioOrganizador.getExtensionTelefonica());

            listUsuariosOrganizador.add(enviarEstadisticasUsuariosItemResponseDTO);
        }
        enviarEstadisticasUsuariosResponseDTO.setListUsuariosOrganizador(listUsuariosOrganizador);

        enviarEstadisticasUsuariosResponseDTO.setTotalUsuariosParticipante(enviarEstadisticasUsuariosServiceDTO.getTotalUsuariosParticipante());
        List<EnviarEstadisticasUsuariosItemResponseDTO> listUsuariosParticipante = new ArrayList<>();
        for(UsuarioDomainEntity usuarioParticipante : enviarEstadisticasUsuariosServiceDTO.getListUsuariosParticipante()){
            EnviarEstadisticasUsuariosItemResponseDTO enviarEstadisticasUsuariosItemResponseDTO = new EnviarEstadisticasUsuariosItemResponseDTO();

            enviarEstadisticasUsuariosItemResponseDTO.setId(usuarioParticipante.getId());
            enviarEstadisticasUsuariosItemResponseDTO.setNombres(usuarioParticipante.getNombres());
            enviarEstadisticasUsuariosItemResponseDTO.setApellidos(usuarioParticipante.getApellidos());
            enviarEstadisticasUsuariosItemResponseDTO.setCorreo(usuarioParticipante.getCorreo());
            enviarEstadisticasUsuariosItemResponseDTO.setDni(usuarioParticipante.getDni());
            enviarEstadisticasUsuariosItemResponseDTO.setCorreoVerificado(usuarioParticipante.getCorreoVerificado());
            enviarEstadisticasUsuariosItemResponseDTO.setCreatedAt(usuarioParticipante.getCreatedAt());
            enviarEstadisticasUsuariosItemResponseDTO.setUpdatedAt(usuarioParticipante.getUpdatedAt());
            enviarEstadisticasUsuariosItemResponseDTO.setTelefono(usuarioParticipante.getTelefono());
            enviarEstadisticasUsuariosItemResponseDTO.setExtensionTelefonica(usuarioParticipante.getExtensionTelefonica());

            listUsuariosParticipante.add(enviarEstadisticasUsuariosItemResponseDTO);
        }
        enviarEstadisticasUsuariosResponseDTO.setListUsuariosParticipante(listUsuariosParticipante);

        return enviarEstadisticasUsuariosResponseDTO;
    }
}
