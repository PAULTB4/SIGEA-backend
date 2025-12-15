package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.module_usuarios.services.serviceDTO.ListarUsuariosServiceDTO;

@Component
public class ListarUsuariosUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final IUsuarioRolRepository usuarioRolRepository;

    public ListarUsuariosUseCase(
        IUsuarioRepository usuarioRepository,
        IUsuarioRolRepository usuarioRolRepository
    ){
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
    }

    public List<ListarUsuariosServiceDTO> execute(){
        
        List<UsuarioDomainEntity> listUsuarioDomainEntities = usuarioRepository.findAll();
        
        List<ListarUsuariosServiceDTO> listListarUsuariosServiceDTOs = new ArrayList<>();
        if(!listUsuarioDomainEntities.isEmpty()){
            for(UsuarioDomainEntity usuarioDomainEntity : listUsuarioDomainEntities){
                ListarUsuariosServiceDTO listarUsuariosServiceDTO = new ListarUsuariosServiceDTO();
    
                listarUsuariosServiceDTO.setId(usuarioDomainEntity.getId());
                listarUsuariosServiceDTO.setNombres(usuarioDomainEntity.getNombres());
                listarUsuariosServiceDTO.setApellidos(usuarioDomainEntity.getApellidos());
                listarUsuariosServiceDTO.setCorreo(usuarioDomainEntity.getCorreo());
                listarUsuariosServiceDTO.setDni(usuarioDomainEntity.getDni());
                listarUsuariosServiceDTO.setCorreoVerificado(usuarioDomainEntity.getCorreoVerificado());
                listarUsuariosServiceDTO.setCreatedAt(usuarioDomainEntity.getCreatedAt());
                listarUsuariosServiceDTO.setUpdatedAt(usuarioDomainEntity.getUpdatedAt());
                listarUsuariosServiceDTO.setTelefono(usuarioDomainEntity.getTelefono());
                listarUsuariosServiceDTO.setExtensionTelefonica(usuarioDomainEntity.getExtensionTelefonica());
    
                List<RolDomainEntity> listRolDomainEntities = usuarioRolRepository.findRolesByUsuarioId(usuarioDomainEntity.getId());
                
                List<String> nombresRoles = new ArrayList<>();
                for(RolDomainEntity rolDomainEntity : listRolDomainEntities){
                    nombresRoles.add(rolDomainEntity.getNombreRol());
                }
                listarUsuariosServiceDTO.setNombresRoles(nombresRoles);

                listListarUsuariosServiceDTOs.add(listarUsuariosServiceDTO);
            }
        }

        return listListarUsuariosServiceDTOs;
    }
}
