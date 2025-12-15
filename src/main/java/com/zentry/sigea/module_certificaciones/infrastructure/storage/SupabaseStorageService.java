package com.zentry.sigea.module_certificaciones.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;

@Service
public class SupabaseStorageService implements StorageService {

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_KEY}")
    private String supabaseKey;

    @Value("${SUPABASE_BUCKET:certificados}")
    private String bucket;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String uploadFile(MultipartFile file, String path) throws Exception {
        String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + path;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        // Usar el tipo MIME real del archivo si está disponible
        String contentType = file.getContentType();
        if (contentType != null && !contentType.isEmpty()) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            // Puedes construir la URL pública según la configuración de Supabase
            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + path;
        } else {
            throw new RuntimeException("Error al subir archivo a Supabase: " + response.getBody());
        }
    }
}
