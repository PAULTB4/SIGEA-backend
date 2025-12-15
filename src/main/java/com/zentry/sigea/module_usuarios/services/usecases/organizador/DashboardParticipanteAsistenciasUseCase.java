package com.zentry.sigea.module_usuarios.services.usecases.organizador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.services.serviceDTO.DashboardParticipanteAsistenciasItemServiceDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.DashboardParticipanteAsistenciasServiceDTO;

@Component
public class DashboardParticipanteAsistenciasUseCase {

    private final IActividadRespository actividadRespository;
    private final ISesionRepository sesionRepository;
    private final IInscripcionRepository inscripcionRepository;
    private final IAsistenciaRepository asistenciaRepository;
    private final IUsuarioRepository usuarioRepository;

    public DashboardParticipanteAsistenciasUseCase(
        IActividadRespository actividadRespository , 
        ISesionRepository sesionRepository,
        IInscripcionRepository inscripcionRepository,
        IAsistenciaRepository asistenciaRepository, 
        IUsuarioRepository usuarioRepository
    ){
        this.actividadRespository = actividadRespository;
        this.sesionRepository = sesionRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<DashboardParticipanteAsistenciasServiceDTO> execute(){
        List<ActividadDomainEntity> listActividadDomainEntities = actividadRespository.findAll();

        List<DashboardParticipanteAsistenciasServiceDTO> listDashboardParticipanteAsistenciasServiceDTOs = new ArrayList<>();
        for(ActividadDomainEntity actividad : listActividadDomainEntities){
            Integer totalInscritosActividad = 0;
            Integer totalAsistentesUltimaSesion = 0;
            DashboardParticipanteAsistenciasServiceDTO dashboardParticipanteAsistenciasServiceDTO = new DashboardParticipanteAsistenciasServiceDTO();

            dashboardParticipanteAsistenciasServiceDTO.setActividadId(actividad.getActividadId());
            dashboardParticipanteAsistenciasServiceDTO.setTituloActividad(actividad.getTitulo());
            dashboardParticipanteAsistenciasServiceDTO.setFechaInicioActividad(actividad.getFechaInicio());
            dashboardParticipanteAsistenciasServiceDTO.setFechaFinActividad(actividad.getFechaFin());
            dashboardParticipanteAsistenciasServiceDTO.setUpdateAtActividad(actividad.getUpdatedAt());
            
            List<InscripcionDomainEntity> listInscripcionDomainEntities = inscripcionRepository.findByActividadId(actividad.getActividadId());
            SesionDomainEntity sesionDomainEntity = sesionRepository.findByOrdenContainingAndActividad_Id("1", actividad.getActividadId())
                .orElse(null);

            if(!listInscripcionDomainEntities.isEmpty() && sesionDomainEntity != null){
                dashboardParticipanteAsistenciasServiceDTO.setSesionId(sesionDomainEntity.getId());
                totalInscritosActividad = listInscripcionDomainEntities.size();

                dashboardParticipanteAsistenciasServiceDTO.setTotalInscritosActividad(totalInscritosActividad);
                dashboardParticipanteAsistenciasServiceDTO.setModalidadActividad(sesionDomainEntity.getModalidad().name());

                List<DashboardParticipanteAsistenciasItemServiceDTO> listDashboardParticipanteAsistenciasItemServiceDTOs = new ArrayList<>();
                for(InscripcionDomainEntity inscripcion : listInscripcionDomainEntities){
                    DashboardParticipanteAsistenciasItemServiceDTO dashboardParticipanteAsistenciasItemServiceDTO = new DashboardParticipanteAsistenciasItemServiceDTO();

                    dashboardParticipanteAsistenciasItemServiceDTO.setInscripcionId(inscripcion.getId());
                    dashboardParticipanteAsistenciasItemServiceDTO.setFechaInscripcion(inscripcion.getFechaInscripcion());

                    UsuarioDomainEntity usuarioDomainEntity = usuarioRepository.findById(inscripcion.getUsuarioId()).orElse(null);

                    if(usuarioDomainEntity != null){
                        dashboardParticipanteAsistenciasItemServiceDTO.setNombresParticipante(usuarioDomainEntity.getNombres());
                        dashboardParticipanteAsistenciasItemServiceDTO.setCorreoParticipante(usuarioDomainEntity.getCorreo());
                        
                    }

                    Boolean presente = asistenciaRepository.findPresenteBySesionIdAndInscripcionId(sesionDomainEntity.getId(), inscripcion.getId());
                    dashboardParticipanteAsistenciasItemServiceDTO.setPresente(presente);
                    if(presente.equals(true)){
                        totalAsistentesUltimaSesion += 1;
                    }
                }
                dashboardParticipanteAsistenciasServiceDTO.setListParticipantesInfo(listDashboardParticipanteAsistenciasItemServiceDTOs);
            }
            Double tasaAsistenciasUltimaSesion = 0.0;
            if(totalInscritosActividad != 0){
                tasaAsistenciasUltimaSesion = (totalAsistentesUltimaSesion.doubleValue() / totalInscritosActividad.doubleValue()) * 100;
            }
            dashboardParticipanteAsistenciasServiceDTO.setTasaAsistenciasUltimaSesion(tasaAsistenciasUltimaSesion);

            listDashboardParticipanteAsistenciasServiceDTOs.add(dashboardParticipanteAsistenciasServiceDTO);
        }
        return listDashboardParticipanteAsistenciasServiceDTOs;
    }
}