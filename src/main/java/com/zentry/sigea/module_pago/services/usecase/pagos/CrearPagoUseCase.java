package com.zentry.sigea.module_pago.services.usecase.pagos;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IEstadoPagoRepository;
import com.zentry.sigea.module_pago.core.repository.IMetodoPagoRepository;
import com.zentry.sigea.module_pago.core.repository.IPagoRepository;
import com.zentry.sigea.module_pago.infrastucture.database.mappers.PagoMapper;
import com.zentry.sigea.module_pago.presentation.models.requestDTO.PagoRequest;

@Component
public class CrearPagoUseCase {
    private final IPagoRepository pagoRepository;
    private final IMetodoPagoRepository metodoPagoRepository;
    private final IEstadoPagoRepository estadoPagoRepository;

    public CrearPagoUseCase(IPagoRepository pagoRepository, IMetodoPagoRepository metodoPagoRepository,
            IEstadoPagoRepository estadoPagoRepository) {
        this.pagoRepository = pagoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.estadoPagoRepository = estadoPagoRepository;
    }

    public PagoRequest execute(PagoRequest request) {
        PagoDomainEntity pagoDomainEntity = PagoMapper.toDomain(request,
                metodoPagoRepository.findById(request.metodoPagoId().toString()),
                estadoPagoRepository.findById(request.estadoPagoId().toString()));
        pagoDomainEntity.setFechaPago(java.time.OffsetDateTime.now());
        pagoRepository.save(pagoDomainEntity);
        return request;
    }

}
