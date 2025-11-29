package com.zentry.sigea.module_usuarios.services.usecases.participante;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IRolRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;

@Component
public class RegistrarParticipanteUseCase {
    
    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final IUsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrarParticipanteUseCase(
        IUsuarioRepository usuarioRepository,
        IRolRepository rolRepository,
        IUsuarioRolRepository usuarioRolRepository , 
        PasswordEncoder passwordEncoder
    , RolJPARepository rolJPARepository){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String execute(UsuarioDomainEntity usuarioDomainEntity){
        String nombreRol = "PARTICIPANTE";

        String rolParticipanteId = rolRepository.findIdByNombreRol(nombreRol);

        if(rolParticipanteId == null){
            throw new RuntimeException("No se encontro el ID de rol para participantes.");
        }

        usuarioDomainEntity.setPasswordHash(
            passwordEncoder.encode(usuarioDomainEntity.getPasswordHash())
        );

        usuarioRepository.save(usuarioDomainEntity);

        String savedUsuarioId = usuarioRepository.findIdByCorreo(usuarioDomainEntity.getCorreo());

        if (savedUsuarioId == null) {
            throw new RuntimeException("No se encontro el ID del usuario registrado.");
        }

        usuarioRolRepository.save(
            savedUsuarioId , 
            rolParticipanteId
        );

        return "Participante registrado con exito";

    }

}
