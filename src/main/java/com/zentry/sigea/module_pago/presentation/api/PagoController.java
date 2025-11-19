package com.zentry.sigea.module_pago.presentation.api;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.zentry.sigea.module_pago.presentation.model.requestDTO.ConsultPagoRequest;
import com.zentry.sigea.module_pago.presentation.model.requestDTO.YapePaymentRequest;
import com.zentry.sigea.module_pago.services.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Crear pago con Yape", description = "Procesa un pago usando Yape a través de MercadoPago")
    public Object pagarConYape(@RequestBody YapePaymentRequest request) {
        return pagoService.pagarConYape(request.getMonto(), request.getDescripcion());
    }
    
    /**
     * Consulta el estado de un pago
     */
    @PostMapping("/consultar-pago")
    @Operation(summary = "Consultar estado de pago", description = "Obtiene el estado actual de un pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado consultado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Object consultarPago(@RequestBody ConsultPagoRequest request) {
        log.info("Consultando pago ID: {}", request.getPaymentId());
        return pagoService.consultarPago(convertToMap(request));
    }

    /**
     * Endpoint de prueba con datos hardcodeados
     */
    @PostMapping("/test-endpoint")
    @Operation(summary = "Endpoint de prueba de pago", description = "Realiza un pago de prueba con datos hardcodeados para verificar la integración con MercadoPago")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago de prueba procesado correctamente"),
    })
    public Object testEndpoint() {
        // La configuración se hace automáticamente a través del servicio

        PaymentClient client = new PaymentClient();

        PaymentCreateRequest createRequest =
            PaymentCreateRequest.builder()
                .description("Titulo del producto")
                .installments(1)
                .payer(PaymentPayerRequest.builder()
                    .email("test_user_123@testuser.com")
                    .build())
                .paymentMethodId("yape")
                .token("ff8080814c11e237014c1ff593b57b4d")
                .transactionAmount(new BigDecimal("5000"))
                .build();

        try {
            client.create(createRequest);
        } catch (Exception e) {
            log.error("Error en endpoint de prueba: {}", e.getMessage(), e);
        }
        return null;
    }

    // // Métodos helper para convertir DTOs a Map (temporalmente)
    // private Map<String, Object> convertToMap(YapePaymentRequest request) {
    //     Map<String, Object> map = new java.util.HashMap<>();
    //     map.put("descripcion", request.getDescripcion());
    //     map.put("monto", request.getMonto());
    //     map.put("token", request.getToken());
    //     map.put("email", request.getEmail());
    //     map.put("referencia", request.getReferencia());
    //     map.put("usuarioId", request.getUsuarioId());
    //     return map;
    // }

    private Map<String, Object> convertToMap(ConsultPagoRequest request) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("paymentId", request.getPaymentId());
        return map;
    }
}
