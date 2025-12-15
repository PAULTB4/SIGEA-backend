package com.zentry.sigea.module_pago.presentation.api;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_pago.presentation.models.requestDTO.EstadoPagoRequest;
import com.zentry.sigea.module_pago.services.EstadoPagoService;

import org.slf4j.Logger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/estado-pago")

public class EstadoPagoController {

    private static final Logger log = LoggerFactory.getLogger(EstadoPagoController.class);
    @Autowired
    private EstadoPagoService estadoPagoService;

    @PostMapping("/crear-estado-pago")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_ORGANIZADOR')")
    @Operation(summary = "Crear estado de pago", description = "Crea un nuevo estado de pago", security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
    }, tags = { "Crear" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Object crearEstadoPago(@RequestBody EstadoPagoRequest request) {
        log.info("Creando estado de pago con etiqueta: {}", request.getEtiqueta());
        estadoPagoService.crearEstadoPago(request);
        return "Estado de pago creado exitosamente";
    }

    @GetMapping("/listar-estados-pago")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_ORGANIZADOR')")
    @Operation(summary = "Listar estados de pago", description = "Obtiene todos los estados de pago", security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
    }, tags = { "Listar" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estados de pago listados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Object listarEstadosPago() {
        return estadoPagoService.listarEstadosPago();
    }

}
