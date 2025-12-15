package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;

@Component
public class CambiarRolUsuarioUseCase {
    private final IUsuarioRolRepository usuarioRolRepository;

    public CambiarRolUsuarioUseCase(
        IUsuarioRolRepository usuarioRolRepository
    ){
        this.usuarioRolRepository = usuarioRolRepository;
    }

    public String execute(String usuarioId , String newRolId , String oldRolId){
        usuarioRolRepository.update(usuarioId , newRolId , oldRolId);

        return "Rol modificado con exito.";
    }
}
