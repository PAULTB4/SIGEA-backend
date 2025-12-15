package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import java.util.ArrayList;
import java.util.List;

import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.DashboardParticipanteAsistenciasItemResponseDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.DashboardParticipanteAsistenciasResponseDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.DashboardParticipanteAsistenciasItemServiceDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.DashboardParticipanteAsistenciasServiceDTO;

public class DashboardParticipanteAsistenciasMapper {
    public static DashboardParticipanteAsistenciasResponseDTO servicetoResponse(DashboardParticipanteAsistenciasServiceDTO dashboardParticipanteAsistenciasServiceDTO){
        DashboardParticipanteAsistenciasResponseDTO dashboardParticipanteAsistenciasResponseDTO = new DashboardParticipanteAsistenciasResponseDTO();

        dashboardParticipanteAsistenciasResponseDTO.setActividadId(dashboardParticipanteAsistenciasServiceDTO.getActividadId());
        dashboardParticipanteAsistenciasResponseDTO.setTituloActividad(dashboardParticipanteAsistenciasServiceDTO.getTituloActividad());
        dashboardParticipanteAsistenciasResponseDTO.setFechaInicioActividad(dashboardParticipanteAsistenciasServiceDTO.getFechaInicioActividad());
        dashboardParticipanteAsistenciasResponseDTO.setFechaFinActividad(dashboardParticipanteAsistenciasServiceDTO.getFechaFinActividad());
        dashboardParticipanteAsistenciasResponseDTO.setModalidadActividad(dashboardParticipanteAsistenciasServiceDTO.getModalidadActividad());
        dashboardParticipanteAsistenciasResponseDTO.setTotalInscritosActividad(dashboardParticipanteAsistenciasServiceDTO.getTotalInscritosActividad());
        dashboardParticipanteAsistenciasResponseDTO.setAsistentesUltimaSesion(dashboardParticipanteAsistenciasServiceDTO.getAsistentesUltimaSesion());
        dashboardParticipanteAsistenciasResponseDTO.setTasaAsistenciasUltimaSesion(dashboardParticipanteAsistenciasServiceDTO.getTasaAsistenciasUltimaSesion());

        dashboardParticipanteAsistenciasResponseDTO.setSesionId(dashboardParticipanteAsistenciasServiceDTO.getSesionId());

        List<DashboardParticipanteAsistenciasItemResponseDTO> listDashboardParticipanteAsistenciasItemResponseDTOs = new ArrayList<>();
        if(!dashboardParticipanteAsistenciasServiceDTO.getListParticipantesInfo().isEmpty()){
            for(DashboardParticipanteAsistenciasItemServiceDTO itemServiceDTO : dashboardParticipanteAsistenciasServiceDTO.getListParticipantesInfo()){
                DashboardParticipanteAsistenciasItemResponseDTO dashboardParticipanteAsistenciasItemResponseDTO = new DashboardParticipanteAsistenciasItemResponseDTO();
    
                dashboardParticipanteAsistenciasItemResponseDTO.setInscripcionId(itemServiceDTO.getInscripcionId());
                dashboardParticipanteAsistenciasItemResponseDTO.setFechaInscripcion(itemServiceDTO.getFechaInscripcion());
                dashboardParticipanteAsistenciasItemResponseDTO.setNombresParticipante(itemServiceDTO.getNombresParticipante());
                dashboardParticipanteAsistenciasItemResponseDTO.setCorreoParticipante(itemServiceDTO.getCorreoParticipante());
                dashboardParticipanteAsistenciasItemResponseDTO.setPresente(itemServiceDTO.getPresente());
            
                listDashboardParticipanteAsistenciasItemResponseDTOs.add(dashboardParticipanteAsistenciasItemResponseDTO);
            }
            dashboardParticipanteAsistenciasResponseDTO.setListParticipantesInfo(listDashboardParticipanteAsistenciasItemResponseDTOs);
        }

        return dashboardParticipanteAsistenciasResponseDTO;
    }
}
