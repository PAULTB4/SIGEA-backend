package com.zentry.sigea.module_informe.services;

import com.zentry.sigea.module_informe.presentation.models.requestDTO.CrearInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.requestDTO.ActualizarInformeRequest;
import com.zentry.sigea.module_informe.presentation.models.responseDTO.InformeResponse;
import com.zentry.sigea.module_informe.services.usecases.informe.CrearInformeUseCase;
import com.zentry.sigea.module_informe.services.usecases.informe.ActualizarInformeUseCase;
import com.zentry.sigea.module_informe.services.usecases.informe.EliminarInformeUseCase;
import com.zentry.sigea.module_informe.services.usecases.informe.ListarInformesUseCase;
import com.zentry.sigea.module_informe.services.usecases.informe.ObtenerInformeUseCase;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@Service
public class InformeService {

    private final CrearInformeUseCase crearInformeUseCase;
    private final ActualizarInformeUseCase actualizarInformeUseCase;
    private final EliminarInformeUseCase eliminarInformeUseCase;
    private final ListarInformesUseCase listarInformesUseCase;
    private final ObtenerInformeUseCase obtenerInformeUseCase;

    private static final String UPLOAD_DIR = "uploads/";

    public InformeService(
        CrearInformeUseCase crearInformeUseCase,
        ActualizarInformeUseCase actualizarInformeUseCase,
        EliminarInformeUseCase eliminarInformeUseCase,
        ListarInformesUseCase listarInformesUseCase,
        ObtenerInformeUseCase obtenerInformeUseCase
    ) {
        this.crearInformeUseCase = crearInformeUseCase;
        this.actualizarInformeUseCase = actualizarInformeUseCase;
        this.eliminarInformeUseCase = eliminarInformeUseCase;
        this.listarInformesUseCase = listarInformesUseCase;
        this.obtenerInformeUseCase = obtenerInformeUseCase;
    }

    public String guardarArchivo(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String filename = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }
    
    public InformeResponse crearInforme(CrearInformeRequest request) {
        return crearInformeUseCase.execute(request);
    }

    @Transactional(readOnly = true)
    public List<InformeResponse> listarInformes() {
        return listarInformesUseCase.execute();
    }

    @Transactional
    public InformeResponse actualizarInforme(String id, ActualizarInformeRequest request) {
        return actualizarInformeUseCase.execute(id, request);
    }

    public InformeResponse obtenerInformePorId(String id) {
        return obtenerInformeUseCase.execute(id);
    }

    @Transactional
    public void eliminarInforme(String id) {
        eliminarInformeUseCase.execute(id);
    }
}
