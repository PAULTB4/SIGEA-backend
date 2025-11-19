package com.zentry.sigea.module_pago.services.interfaces;

import java.math.BigDecimal;
import java.util.Map;

public interface IPago {

    Object pagarConYape(BigDecimal monto, String descripcion);
    Object consultarPago(Object request);
    Map<String, Object> createMetadata(Map<String, Object> request);
}
