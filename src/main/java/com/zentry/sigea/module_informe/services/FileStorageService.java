package com.zentry.sigea.module_informe.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileStorageService {

    @Value("${media.location}")
    private String mediaLocation;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(mediaLocation);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        String filename = file.getOriginalFilename();
        Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Fallo al guardar archivo", e);
        }
    }

    public void guardarArchivos(String informeId, List<MultipartFile> archivos) {
        try {
            Path informeDir = rootLocation.resolve(informeId);
            if (!Files.exists(informeDir)) {
                Files.createDirectories(informeDir);
            }
            for (MultipartFile archivo : archivos) {
                Path filePath = informeDir.resolve(archivo.getOriginalFilename());
                archivo.transferTo(filePath.toFile());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivos", e);
        }
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("No se pudo leer el archivo: " + filename, e);
        }
    }

    public Resource cargarArchivoDeInforme(String informeId, String filename) {
        try {
            Path file = rootLocation.resolve(informeId).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("No se pudo leer el archivo: " + filename, e);
        }
    }
}
