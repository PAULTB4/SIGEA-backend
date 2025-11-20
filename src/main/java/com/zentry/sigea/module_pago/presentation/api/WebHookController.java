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

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebHookController.class);

    @PostMapping
    public ResponseEntity<String> recibirWebHook(@RequestBody WebHookResponse entity) {
        
        logger.info("WebHook recibido: " + entity);
        return ResponseEntity.ok("WebHook recibido");
    }
    
    
}
