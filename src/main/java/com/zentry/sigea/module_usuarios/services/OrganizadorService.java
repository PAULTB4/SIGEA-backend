package com.zentry.sigea.module_usuarios.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.services.serviceDTO.DashboardParticipanteAsistenciasServiceDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaServiceDTO;
import com.zentry.sigea.module_usuarios.services.usecases.organizador.DashboardParticipanteAsistenciasUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.organizador.RegistrarAsistenciaMasivaUseCase;

@Service
public class OrganizadorService {
    
    private final RegistrarAsistenciaMasivaUseCase registrarAsistenciaUseCase;
    private final DashboardParticipanteAsistenciasUseCase dashboardParticipanteAsistenciasUseCase;

    public OrganizadorService(
        RegistrarAsistenciaMasivaUseCase registrarAsistenciaUseCase,
        DashboardParticipanteAsistenciasUseCase dashboardParticipanteAsistenciasUseCase
    ){
        this.registrarAsistenciaUseCase = registrarAsistenciaUseCase;
        this.dashboardParticipanteAsistenciasUseCase = dashboardParticipanteAsistenciasUseCase;
    }

    public String registrarAsistencia(
        RegistrarAsistenciaServiceDTO registrarAsistenciaServiceDTO
    ){
        return registrarAsistenciaUseCase.execute(registrarAsistenciaServiceDTO);
    }


    public List<DashboardParticipanteAsistenciasServiceDTO> dashboardParticipanteAsistencias(){
        return dashboardParticipanteAsistenciasUseCase.execute();
    }
}
