package com.zentry.sigea.module_pago.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mercadopago.MercadoPagoConfig;

/**
 * Configuración centralizada para Mercado Pago
 * Inicializa el SDK con las credenciales del ambiente
 */
@Configuration
public class MercadoPagoConfigurer {

    @Value("${mercado.pago.access-token}")
    private String accessToken;

    @Value("${mercado.pago.public-key}")
    private String publicKey;

    public MercadoPagoConfigurer() {
        // Inicializar configuración al cargar la clase
        initializeMercadoPago();
    }

    /**
     * Inicializa el SDK de Mercado Pago con las credenciales
     */
    public void initializeMercadoPago() {
        try {
            if (accessToken != null && !accessToken.isEmpty()) {
                MercadoPagoConfig.setAccessToken(accessToken);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al configurar Mercado Pago: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna la clave pública para uso en frontend
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Retorna el access token (solo para uso interno del backend)
     */
    public String getAccessToken() {
        return accessToken;
    }
}
