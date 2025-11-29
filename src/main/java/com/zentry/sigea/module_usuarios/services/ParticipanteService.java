package com.zentry.sigea.module_usuarios.services;

import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.services.usecases.participante.RegistrarParticipanteUseCase;

@Service
public class ParticipanteService {
    
    private final RegistrarParticipanteUseCase registrarParticipanteUseCase;

    public ParticipanteService(
        RegistrarParticipanteUseCase registrarParticipanteUseCase
    ) {
        this.registrarParticipanteUseCase = registrarParticipanteUseCase;
    }

    public String registrarParticipante(UsuarioDomainEntity usuarioDomainEntity){
        return registrarParticipanteUseCase.execute(usuarioDomainEntity);
    }
}
