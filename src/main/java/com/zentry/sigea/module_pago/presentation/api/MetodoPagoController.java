package com.zentry.sigea.module_pago.presentation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_pago.presentation.models.requestDTO.MetodoPagoRequest;
import com.zentry.sigea.module_pago.services.MetodoPagoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/metodo-pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;
    private static final Logger log = LoggerFactory.getLogger(MetodoPagoController.class);

    @PostMapping("/crear-metodo-pago")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR')")
    @Operation(summary = "Crear metodo de pago", description = "Crea un nuevo metodo de pago", security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
    }, tags = { "Crear" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Object crearTipoPago(@RequestBody MetodoPagoRequest request) {
        log.info("Creando tipo de pago con etiqueta: {}", request.getEtiqueta());
        metodoPagoService.crearMetodoPago(request);
        return "Tipo de pago creado exitosamente";
    }

    @GetMapping("/listar-metodos-pago")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ORGANIZADOR')")
    @Operation(summary = "Listar metodos de pago", description = "Obtiene todos los metodos de pago", security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT")
    }, tags = { "Listar" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metodos de pago listados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Object listarMetodosPago() {
        return metodoPagoService.listarMetodosPago();
    }

}
