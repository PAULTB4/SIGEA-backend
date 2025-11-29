package com.zentry.sigea.module_usuarios.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.CrearRolUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.RegisterUsuarioUseCase;

@Service
public class AdministradorService {

    private final RegisterUsuarioUseCase registerUsuarioUseCase;
    private final CrearRolUseCase crearRolUseCase;

    public AdministradorService(
        RegisterUsuarioUseCase registerUsuarioUseCase , 
        CrearRolUseCase crearRolUseCase
    ){
        this.registerUsuarioUseCase = registerUsuarioUseCase;
        this.crearRolUseCase = crearRolUseCase;
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
}
