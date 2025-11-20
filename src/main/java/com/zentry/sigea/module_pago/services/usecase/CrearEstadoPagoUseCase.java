package com.zentry.sigea.module_pago.services.usecase;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IEstadoPagoRepository;
import com.zentry.sigea.module_pago.presentation.model.requestDTO.EstadoPagoRequest;

@Component
public class CrearEstadoPagoUseCase {

    private final IEstadoPagoRepository estadoPagoRepository;

    public CrearEstadoPagoUseCase(IEstadoPagoRepository estadoPagoRepository) {
        this.estadoPagoRepository = estadoPagoRepository;
    }

    
    public String execute(EstadoPagoRequest request) {


        // Crear la entidad usando el factory method del dominio
        var nuevoEstadoPago = EstadoPagoDomainEntity.create(
            request.getDescripcion(),
            request.getEtiqueta()
        );

        // Guardar usando el repositorio
        return estadoPagoRepository.save(nuevoEstadoPago) ? 
            "Estado de pago registrado con éxito" : 
            "Algo salió mal al guardar el estado de pago";
        
    }

    
}