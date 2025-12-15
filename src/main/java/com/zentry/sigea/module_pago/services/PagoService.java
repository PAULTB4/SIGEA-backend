package com.zentry.sigea.module_pago.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.zentry.sigea.module_pago.config.MercadoPagoConfigurer;
import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.PagoRequest;
import com.zentry.sigea.module_pago.services.interfaces.IPago;
import com.zentry.sigea.module_pago.services.usecase.pagos.CrearPagoUseCase;
import com.zentry.sigea.module_pago.services.usecase.pagos.ListarPagoUseCase;

@Service
public class PagoService implements IPago {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final MercadoPagoConfigurer mercadoPagoConfigurer;
    private final CrearPagoUseCase crearPagoUseCase;
    private final ListarPagoUseCase listarPagoUseCase;

    public PagoService(MercadoPagoConfigurer mercadoPagoConfigurer, CrearPagoUseCase crearPagoUseCase,
            ListarPagoUseCase listarPagoUseCase) {
        this.mercadoPagoConfigurer = mercadoPagoConfigurer;
        this.crearPagoUseCase = crearPagoUseCase;
        this.listarPagoUseCase = listarPagoUseCase;
    }

    @Override
    public Object pagarConYape(BigDecimal monto, String descripcion) {
        try {
            // Configurar MercadoPago
            mercadoPagoConfigurer.initializeMercadoPago();

            // Crear cliente de preferencia
            PreferenceClient client = new PreferenceClient();

            // Crear item del producto/servicio
            List<PreferenceItemRequest> items = new ArrayList<>();
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id("001")
                    .title("Pago con Yape - Servicio SIGEA")
                    .description(descripcion)
                    .pictureUrl("https://via.placeholder.com/300x200.png?text=SIGEA")
                    .categoryId("services")
                    .quantity(1)
                    .currencyId("PEN") // Soles peruanos
                    .unitPrice(monto) // Monto dinámico
                    .build();
            items.add(item);

            // URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://brennisc.github.io/url_sigea?status=success")
                    .pending("https://brennisc.github.io/url_sigea?status=pending")
                    .failure("https://brennisc.github.io/url_sigea?status=failure")
                    .build();

            // Configurar métodos de pago (solo Yape)
            List<PreferencePaymentMethodRequest> excludedPaymentMethods = new ArrayList<>();
            // Excluir todos los métodos excepto Yape
            excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("visa").build());
            excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("master").build());
            excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("amex").build());
            excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("diners").build());
            excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("cabal").build());
            PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                    .excludedPaymentMethods(excludedPaymentMethods)
                    .installments(1) // Solo 1 cuota para Yape
                    .build();

            // Información del pagador
            PreferencePayerRequest payer = PreferencePayerRequest.builder()
                    .name("compradortest")
                    .email("brennisbenjaminn@gmail.com")
                    .build();

            // Crear la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .paymentMethods(paymentMethods)
                    .payer(payer)
                    .autoReturn("approved")
                    .externalReference("SIGEA-" + System.currentTimeMillis())
                    .build();

            Preference preference = client.create(preferenceRequest);

            // Preparar respuesta con URLs
            Map<String, Object> response = new HashMap<>();
            response.put("preference_id", preference.getId());
            response.put("init_point", preference.getInitPoint()); // URL para web
            response.put("sandbox_init_point", preference.getSandboxInitPoint()); // URL para sandbox
            response.put("status", "success");
            response.put("message", "URL de pago generada exitosamente");

            log.info("Preferencia creada exitosamente: ID = {}", preference.getId());
            log.info("URL de pago: {}", preference.getInitPoint());

            return response;

        } catch (MPApiException apiException) {
            log.error("Error de API de MercadoPago:");
            log.error("Status: {}", apiException.getStatusCode());
            log.error("Mensaje: {}", apiException.getApiResponse().getContent());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error al crear preferencia de pago");
            errorResponse.put("details", apiException.getApiResponse().getContent());
            return errorResponse;

        } catch (MPException exception) {
            log.error("Error de MercadoPago: {}", exception.getMessage());
            exception.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error interno de MercadoPago");
            errorResponse.put("details", exception.getMessage());
            return errorResponse;
        }
    }

    @Override
    public Object consultarPago(Object request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultarPago'");
    }

    @Override
    public Map<String, Object> createMetadata(Map<String, Object> request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMetadata'");
    }

    @Override
    public Object guardarPago(PagoRequest request) {
        return crearPagoUseCase.execute(request);
    }

    @Override
    public List<PagoDomainEntity> findAll() {
        return listarPagoUseCase.execute();
    }

}