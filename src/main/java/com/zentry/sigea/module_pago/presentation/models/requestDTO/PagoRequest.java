package com.zentry.sigea.module_pago.presentation.models.requestDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record PagoRequest(
        UUID inscripcionId,
        BigDecimal monto,
        String estadoPagoId,
        String moneda,
        UUID metodoPagoId,
        String referenciaExterna, // opcional, para pagos externos
        String descripcion) {
}
