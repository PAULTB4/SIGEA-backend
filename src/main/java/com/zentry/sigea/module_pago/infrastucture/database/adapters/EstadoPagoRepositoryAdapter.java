package com.zentry.sigea.module_pago.infrastucture.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_pago.core.entities.EstadoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IEstadoPagoRepository;
import com.zentry.sigea.module_pago.infrastucture.database.mappers.EstadoPagoMapper;
import com.zentry.sigea.module_pago.infrastucture.repository.EstadoPagoJPARepository;

@Repository
public class EstadoPagoRepositoryAdapter implements IEstadoPagoRepository {

    private final EstadoPagoJPARepository estadoPagoJPARepository;
    public EstadoPagoRepositoryAdapter(EstadoPagoJPARepository estadoPagoJPARepository) {
        this.estadoPagoJPARepository = estadoPagoJPARepository;
    }

    public boolean save(EstadoPagoDomainEntity estadoPagoDomainEntity) {
        try {
            estadoPagoJPARepository.save(EstadoPagoMapper.toEntity(estadoPagoDomainEntity));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<EstadoPagoDomainEntity> findById(String id) {
        return estadoPagoJPARepository.findById(UUID.fromString(id))
            .map(EstadoPagoMapper::toDomainEntity);
    }

    public List<EstadoPagoDomainEntity> findAll() {
        return estadoPagoJPARepository.findAll()
            .stream()
            .map(EstadoPagoMapper::toDomainEntity)
            .collect(java.util.stream.Collectors.toList());
    }

    public void deleteById(String id) {
        estadoPagoJPARepository.deleteById(UUID.fromString(id));
    }


}
