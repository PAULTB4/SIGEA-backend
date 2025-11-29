package com.zentry.sigea.module_inscripciones.presentation.models.mappers;

import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.CrearInscripcionRequest;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;

public class CrearInscripcionMapper {
    public static CrearInscripcionServiceDTO requestToService(CrearInscripcionRequest crearInscripcionRequest){
        
        CrearInscripcionServiceDTO crearInscripcionServiceDTO = new CrearInscripcionServiceDTO();

        crearInscripcionServiceDTO.setFechaInscripcion(crearInscripcionRequest.getFechaInscripcion());
        crearInscripcionServiceDTO.setUsuarioId(crearInscripcionRequest.getUsuarioId());
        crearInscripcionServiceDTO.setActividadId(crearInscripcionRequest.getActividadId());
        crearInscripcionServiceDTO.setEstadoId(crearInscripcionRequest.getEstadoId());

        return crearInscripcionServiceDTO;
    }
}
