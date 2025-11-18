package com.zentry.sigea.module_asistencias.services.interfaces;

import java.util.List;

import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;

public interface IAsistenciaService {
    
    String registrarAsistencia(RegistrarAsistenciaRequest request);
    
    List<AsistenciaResponse> listarAsistenciasPorSesion(String sesionId);
    
    List<AsistenciaResponse> listarAsistenciasPorInscripcion(String inscripcionId);
    
    List<AsistenciaResponse> listarPresentesPorSesion(String sesionId);
    
    AsistenciaResponse obtenerAsistenciaPorId(String id);
    
    AsistenciaResponse actualizarEstadoAsistencia(String id, Boolean presente);
}
