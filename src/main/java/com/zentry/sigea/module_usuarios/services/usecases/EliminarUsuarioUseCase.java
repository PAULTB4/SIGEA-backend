package com.zentry.sigea.module_usuarios.services.usecases;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;

@Component
public class EliminarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(
        IUsuarioRepository usuarioRepository
    ){
        this.usuarioRepository = usuarioRepository;
    }

    public String execute(String usuarioId){
        usuarioRepository.deleteById(usuarioId);

        return "Usuario eliminado con exito.";
    }
}
