package com.zentry.sigea.module_usuarios.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.services.serviceDTO.EnviarEstadisticasUsuariosServiceDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.ListarUsuariosServiceDTO;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.CambiarRolUsuarioUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.CrearRolUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.EditarRolUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.EliminarRolUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.EnviarEstadisticasUsuariosUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.ListarRolesUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.ListarUsuariosUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.RegisterUsuarioUseCase;

@Service
public class AdministradorService {

    private final RegisterUsuarioUseCase registerUsuarioUseCase;
    private final CrearRolUseCase crearRolUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final EnviarEstadisticasUsuariosUseCase enviarEstadisticasUsuariosUseCase;
    private final ListarRolesUseCase listarRolesUseCase;
    private final EditarRolUseCase editarRolUseCase;
    private final EliminarRolUseCase eliminarRolUseCase;
    private final CambiarRolUsuarioUseCase cambiarRolUsuarioUseCase;

    public AdministradorService(
        RegisterUsuarioUseCase registerUsuarioUseCase , 
        CrearRolUseCase crearRolUseCase , 
        ListarUsuariosUseCase listarUsuariosUseCase , 
        EnviarEstadisticasUsuariosUseCase enviarEstadisticasUsuariosUseCase , 
        ListarRolesUseCase listarRolesUseCase,
        EditarRolUseCase editarRolUseCase,
        EliminarRolUseCase eliminarRolUseCase,
        CambiarRolUsuarioUseCase cambiarRolUsuarioUseCase
    ){
        this.registerUsuarioUseCase = registerUsuarioUseCase;
        this.crearRolUseCase = crearRolUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.enviarEstadisticasUsuariosUseCase = enviarEstadisticasUsuariosUseCase;
        this.listarRolesUseCase = listarRolesUseCase;
        this.editarRolUseCase = editarRolUseCase;
        this.eliminarRolUseCase = eliminarRolUseCase;
        this.cambiarRolUsuarioUseCase = cambiarRolUsuarioUseCase;
    }

    public String registerUsuario(
        UsuarioDomainEntity usuarioDomainEntity , 
        List<String> listRolesId
    ) throws IOException {
        return registerUsuarioUseCase.execute(usuarioDomainEntity, listRolesId);
    }

    public String crearRol(
        RolDomainEntity rolDomainEntity
    ){
        return crearRolUseCase.execute(rolDomainEntity);
    }

    public List<RolDomainEntity> listarRoles(){
        return listarRolesUseCase.execute();
    }
    
    public String editarRol(
        String id , 
        String nombreRol , 
        String descripcion
    ){
        return editarRolUseCase.execute(
            id, 
            nombreRol , 
            descripcion
        );
    }

    public List<ListarUsuariosServiceDTO> listarUsuarios(){
        return listarUsuariosUseCase.execute();
    }

    public EnviarEstadisticasUsuariosServiceDTO enviarEstadisticasUsuarios(){
        return enviarEstadisticasUsuariosUseCase.execute();
    }

    public String eliminarRol(String id){
        return eliminarRolUseCase.execute(id);
    }
    public String cambiarRolUsuario(String usuarioId , String newRolId , String oldRolId){
        return cambiarRolUsuarioUseCase.execute(usuarioId, newRolId, oldRolId);
    }
}
