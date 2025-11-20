package com.zentry.sigea.module_pago.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_pago.presentation.model.responseDTO.WebHookResponse;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/webhook")
@Tag(name = "WebHook", description = "API para gestión de WebHooks")
public class WebHookController {

    @PostMapping
    public ResponseEntity<String> recibirWebHook(@RequestBody WebHookResponse entity) {
        System.out.println("WebHook recibido: " + entity.getType());
        System.out.println("ID del evento: " + entity.getData().getId());
        return ResponseEntity.ok("WebHook recibido");
    }
    
    
}
