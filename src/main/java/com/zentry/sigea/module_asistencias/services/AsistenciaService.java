package com.zentry.sigea.module_asistencias.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.presentation.models.mappers.AsistenciaPresentationMapper;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;
import com.zentry.sigea.module_asistencias.services.interfaces.IAsistenciaService;
import com.zentry.sigea.module_asistencias.services.usecases.ActualizarAsistenciaUseCase;
import com.zentry.sigea.module_asistencias.services.usecases.RegistrarAsistenciaUseCase;
import com.zentry.sigea.module_asistencias.services.usecases.ListarAsistenciasUseCase;
import com.zentry.sigea.module_asistencias.services.usecases.ObtenerAsistenciaPorIdUseCase;

@Service
@Transactional
public class AsistenciaService implements IAsistenciaService {

    private final RegistrarAsistenciaUseCase registrarAsistenciaUseCase;
    private final ListarAsistenciasUseCase listarAsistenciasUseCase;
    private final ObtenerAsistenciaPorIdUseCase obtenerAsistenciaPorIdUseCase;
    private final ActualizarAsistenciaUseCase actualizarAsistenciaUseCase;

    public AsistenciaService(
        RegistrarAsistenciaUseCase registrarAsistenciaUseCase,
        ListarAsistenciasUseCase listarAsistenciasUseCase,
        ObtenerAsistenciaPorIdUseCase obtenerAsistenciaPorIdUseCase,
        ActualizarAsistenciaUseCase actualizarAsistenciaUseCase
    ) {
        this.registrarAsistenciaUseCase = registrarAsistenciaUseCase;
        this.listarAsistenciasUseCase = listarAsistenciasUseCase;
        this.obtenerAsistenciaPorIdUseCase = obtenerAsistenciaPorIdUseCase;
        this.actualizarAsistenciaUseCase = actualizarAsistenciaUseCase;
    }

    @Override
    public String registrarAsistencia(RegistrarAsistenciaRequest request) {
        return registrarAsistenciaUseCase.execute(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponse> listarAsistenciasPorSesion(String sesionId) {
        return listarAsistenciasUseCase.executeBySesion(sesionId)
            .stream()
            .map(AsistenciaPresentationMapper::domainToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponse> listarAsistenciasPorInscripcion(String inscripcionId) {
        return listarAsistenciasUseCase.executeByInscripcion(inscripcionId)
            .stream()
            .map(AsistenciaPresentationMapper::domainToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponse> listarPresentesPorSesion(String sesionId) {
        return listarAsistenciasUseCase.executeBySesionYEstado(sesionId, true)
            .stream()
            .map(AsistenciaPresentationMapper::domainToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AsistenciaResponse obtenerAsistenciaPorId(String id) {
        AsistenciaDomainEntity asistencia = obtenerAsistenciaPorIdUseCase.execute(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró asistencia con ID: " + id
            ));
        return AsistenciaPresentationMapper.domainToResponse(asistencia);
    }

    @Override
    public AsistenciaResponse actualizarEstadoAsistencia(String id, Boolean presente) {
        AsistenciaDomainEntity asistenciaActualizada = 
            actualizarAsistenciaUseCase.execute(id, presente);
        return AsistenciaPresentationMapper.domainToResponse(asistenciaActualizada);
    }
}

