package com.zentry.sigea.module_usuarios.services.usecases.organizador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaServiceDTO;

@Component
public class RegistrarAsistenciaMasivaUseCase {
    
    private final IAsistenciaRepository asistenciaRepository;

    public RegistrarAsistenciaMasivaUseCase(IAsistenciaRepository asistenciaRepository){
        this.asistenciaRepository = asistenciaRepository;
    }

    public String execute(RegistrarAsistenciaServiceDTO registrarAsistenciaServiceDTO){

        List<AsistenciaDomainEntity> listAsistenciaDomainEntities = new ArrayList<>();

        String sesionId = registrarAsistenciaServiceDTO.getSesionId();

        registrarAsistenciaServiceDTO.getRegistrarAsistenciaItemServiceDTOs().forEach((listOfData) -> {
            AsistenciaDomainEntity asistenciaDomainEntity = AsistenciaDomainEntity.create(
                sesionId , 
                listOfData.getInscripcionId(), 
                listOfData.getPresente(),
                Optional.ofNullable(listOfData.getRegistradoEn())
            );
            listAsistenciaDomainEntities.add(asistenciaDomainEntity);
        });

        asistenciaRepository.saveAll(listAsistenciaDomainEntities);

        return "Asistencia registrada con exito";
    }
}
