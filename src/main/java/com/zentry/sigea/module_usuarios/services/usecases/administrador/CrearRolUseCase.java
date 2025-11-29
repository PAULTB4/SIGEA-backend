package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IRolRepository;

@Component
public class CrearRolUseCase {
    
    private IRolRepository rolRepository;

    public CrearRolUseCase(
        IRolRepository rolRepository
    ){
        this.rolRepository = rolRepository;
    }

    public String execute(RolDomainEntity rolDomainEntity){
        rolRepository.save(rolDomainEntity);

        return "Rol registrado con exito";
    }
}
