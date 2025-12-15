package com.zentry.sigea.module_certificaciones.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * Sube un archivo y retorna la URL pública o el path.
     * @param file archivo a subir
     * @param path ruta destino dentro del bucket
     * @return URL o path del archivo subido
     */
    String uploadFile(MultipartFile file, String path) throws Exception;
}
