package com.zentry.sigea.module_pago.services.usecase;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IEstadoPagoRepository;
import com.zentry.sigea.module_pago.core.repository.IMetodoPagoRepository;
import com.zentry.sigea.module_pago.core.repository.IPagoRepository;
import com.zentry.sigea.module_pago.presentation.model.requestDTO.PagoRequest;

@Component
public class CrearPagoYapeUseCase {

    
    private final IPagoRepository pagoRepository;
    private final IMetodoPagoRepository metodoPagoRepository;
    private final IEstadoPagoRepository estadoPagoRepository;
    public CrearPagoYapeUseCase(IPagoRepository pagoRepository, IMetodoPagoRepository metodoPagoRepository, IEstadoPagoRepository estadoPagoRepository ) {
        this.pagoRepository = pagoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.estadoPagoRepository = estadoPagoRepository;
    }

    public PagoDomainEntity execute(PagoRequest request) {
        // Lógica para crear un pago con Yape
        PagoDomainEntity nuevoPago = new PagoDomainEntity();
        
        boolean isSaved = pagoRepository.save(nuevoPago);
        if (!isSaved) {
            throw new RuntimeException("Error al guardar el pago");
        }

        return nuevoPago;
    }
    
}
