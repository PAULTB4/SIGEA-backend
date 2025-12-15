package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.module_usuarios.services.serviceDTO.EnviarEstadisticasUsuariosServiceDTO;

@Component
public class EnviarEstadisticasUsuariosUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final IUsuarioRolRepository usuarioRolRepository;

    public EnviarEstadisticasUsuariosUseCase(
        IUsuarioRepository usuarioRepository,
        IUsuarioRolRepository usuarioRolRepository
    ){
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
    }

    public EnviarEstadisticasUsuariosServiceDTO execute(){
        List<UsuarioDomainEntity> listUsuarioDomainEntities = usuarioRepository.findAll();
        Integer totalRegisteredUsers = listUsuarioDomainEntities.size();

        List<UsuarioDomainEntity> listUsuarioOrganizador = usuarioRolRepository.findAllUsuariosByNombreRol("ORGANIZADOR");
        Integer totalUsuariosOrganizador = listUsuarioOrganizador.size();
        
        List<UsuarioDomainEntity> listUsuarioParticipante = usuarioRolRepository.findAllUsuariosByNombreRol("PARTICIPANTE");
        Integer totalUsuariosParticipante = listUsuarioParticipante.size();

        EnviarEstadisticasUsuariosServiceDTO enviarEstadisticasUsuariosServiceDTO = new EnviarEstadisticasUsuariosServiceDTO();

        enviarEstadisticasUsuariosServiceDTO.setTotalRegisteredUsers(totalRegisteredUsers);
        enviarEstadisticasUsuariosServiceDTO.setListUsuarioDomainEntities(listUsuarioDomainEntities);
        enviarEstadisticasUsuariosServiceDTO.setTotalUsuariosOrganizador(totalUsuariosOrganizador);
        enviarEstadisticasUsuariosServiceDTO.setListUsuariosOrganizador(listUsuarioOrganizador);
        enviarEstadisticasUsuariosServiceDTO.setTotalUsuariosParticipante(totalUsuariosParticipante);
        enviarEstadisticasUsuariosServiceDTO.setListUsuariosParticipante(listUsuarioParticipante);

        return enviarEstadisticasUsuariosServiceDTO;
    }
}
