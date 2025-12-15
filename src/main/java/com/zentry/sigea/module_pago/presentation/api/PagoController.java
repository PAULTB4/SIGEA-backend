package com.zentry.sigea.module_pago.presentation.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zentry.sigea.module_pago.presentation.models.requestDTO.ConsultPagoRequest;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.PagoRequest;
import com.zentry.sigea.module_pago.presentation.models.responseDTO.PagoResponse;
import com.zentry.sigea.module_pago.services.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "API para gestión de pagos con Yape")
@CrossOrigin(origins = "*")
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    /**
     * Crea un pago directo con Yape
     */
    @PostMapping("/crear-pago-yape")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_ORGANIZADOR', 'ROLE_PARTICIPANTE')")
    @Operation(summary = "Crear pago con Yape", description = "Procesa un pago usando Yape a través de MercadoPago", security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")

    }, responses = {
            @ApiResponse(responseCode = "200", description = "Pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }, tags = { "Crear" })
    public Object pagarConYape(@RequestBody PagoRequest request) {
        log.info("Iniciando pago con Yape para monto: {}", request.monto());
        pagoService.guardarPago(request);
        return pagoService.pagarConYape(request.monto(), request.descripcion());
    }

    /**
     * Consulta el estado de un pago
     */
    @PostMapping("/consultar-pago")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_ORGANIZADOR', 'ROLE_PARTICIPANTE')")
    @Operation(
        summary = "Consultar estado de pago",
        security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
        }, 
        description = "Obtiene el estado actual de un pago por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado consultado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Object consultarPago(@RequestBody ConsultPagoRequest request) {
        log.info("Consultando pago ID: {}", request.getPaymentId());
        return pagoService.consultarPago(request.getPaymentId());
    }

    // // Métodos helper para convertir DTOs a Map (temporalmente)
    // private Map<String, Object> convertToMap(YapePaymentRequest request) {
    // Map<String, Object> map = new java.util.HashMap<>();
    // map.put("descripcion", request.getDescripcion());
    // map.put("monto", request.getMonto());
    // map.put("token", request.getToken());
    // map.put("email", request.getEmail());
    // map.put("referencia", request.getReferencia());
    // map.put("usuarioId", request.getUsuarioId());
    // return map;
    // }

    private Map<String, Object> convertToMap(ConsultPagoRequest request) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("paymentId", request.getPaymentId());
        return map;
    }

    @GetMapping("/listar-pagos")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR', 'ROLE_ORGANIZADOR', 'ROLE_PARTICIPANTE')")
    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos", security = {
            @SecurityRequirement(name = "administradorJWT"),
            @SecurityRequirement(name = "organizadorJWT"),
            @SecurityRequirement(name = "participanteJWT")
    }, tags = "Pagos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos listados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<PagoResponse> listarPagos() {
        log.info("Listando todos los pagos");
        return pagoService.findAll().stream().map(pago -> new PagoResponse(pago)).collect(Collectors.toList());
    }

}
