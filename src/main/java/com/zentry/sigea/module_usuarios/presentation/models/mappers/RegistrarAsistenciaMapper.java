package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarAsistenciaItemRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarAsistenciaRequestDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaItemServiceDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaServiceDTO;

public class RegistrarAsistenciaMapper {
    public static RegistrarAsistenciaServiceDTO requestToService(RegistrarAsistenciaRequestDTO registrarAsistenciaRequestDTO){
        RegistrarAsistenciaServiceDTO registrarAsistenciaServiceDTO = new RegistrarAsistenciaServiceDTO();

        registrarAsistenciaServiceDTO.setSesionId(registrarAsistenciaRequestDTO.getSesionId());

        List<RegistrarAsistenciaItemServiceDTO> listRegistrarAsistenciaItemServiceDTOs = new ArrayList<>();

        for(RegistrarAsistenciaItemRequestDTO registrarAsistenciaItemRequestDTO : registrarAsistenciaRequestDTO.getRegistrarAsistenciaItemRequestDTOs()){
            RegistrarAsistenciaItemServiceDTO registrarAsistenciaItemServiceDTO = new RegistrarAsistenciaItemServiceDTO(
                registrarAsistenciaItemRequestDTO.getInscripcionId() , 
                registrarAsistenciaItemRequestDTO.getPresente() , 
                Optional.of(registrarAsistenciaItemRequestDTO.getRegistradoEn())
            );
            
            listRegistrarAsistenciaItemServiceDTOs.add(registrarAsistenciaItemServiceDTO);
        }

        registrarAsistenciaServiceDTO.setRegistrarAsistenciaItemServiceDTOs(listRegistrarAsistenciaItemServiceDTOs);

        return registrarAsistenciaServiceDTO;
    }
}
