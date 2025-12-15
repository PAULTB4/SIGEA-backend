package com.zentry.sigea.module_inscripciones.services.interfaces;

import java.util.List;

import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.InscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.InscripcionResponse;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;

/**
 * Interfaz del servicio de inscripciones
 */
public interface IInscripcion {
    boolean crearInscripcion(CrearInscripcionServiceDTO crearInscripcionServiceDTO);
    List<InscripcionResponse> listarInscripciones();
    InscripcionResponse actualizarInscripcion(String id, InscripcionRequest request);
    InscripcionResponse obtenerInscripcionPorId(String id);
    List<InscripcionResponse> obtenerInscripcionesPorUsuario(String usuarioId);
    List<InscripcionResponse> obtenerInscripcionesPorActividad(String actividadId);
    void eliminarInscripcion(String id);
}

