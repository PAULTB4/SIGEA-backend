package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import java.io.IOException;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;

@Component
public class RegisterUsuarioUseCase {
    private final PasswordEncoder passwordEncoder;
    private final IUsuarioRepository usuarioRepository;
    private final IUsuarioRolRepository usuarioRolRepository;

    public RegisterUsuarioUseCase(
        PasswordEncoder passwordEncoder , 
        IUsuarioRepository usuarioRepository , 
        IUsuarioRolRepository usuarioRolRepository
    ){
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
    }

    public String execute(UsuarioDomainEntity usuarioDomainEntity , List<String> listRolesId) throws IOException{

        usuarioDomainEntity.setPasswordHash(
            passwordEncoder.encode(usuarioDomainEntity.getPasswordHash())
        );

        usuarioRepository.save(usuarioDomainEntity);

        String registeredUsuarioId = usuarioRepository.findIdByCorreo(usuarioDomainEntity.getCorreo());

        if (registeredUsuarioId == null) {
            throw new IOException("No se pudo encontrar el ID del usuario."); // Siempre debes especificar que la funciona puede arrojar un error
        }

        usuarioRolRepository.saveOneUserWithAllRolesId(
            registeredUsuarioId, 
            listRolesId
        );

        return "Usuario registrado con exito.";
    }
}
