package com.zentry.sigea.module_pago.infrastucture.database.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_pago.core.entities.MetodoPagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IMetodoPagoRepository;
import com.zentry.sigea.module_pago.infrastucture.database.mappers.MetodoPagoMapper;
import com.zentry.sigea.module_pago.infrastucture.repository.MetodoPagoJPARepository;


@Repository
public class MetodoPagoRepositoryAdapter  implements  IMetodoPagoRepository {

    private final MetodoPagoJPARepository metodoPagoJPARepository;
    public MetodoPagoRepositoryAdapter(MetodoPagoJPARepository metodoPagoJPARepository) {
        this.metodoPagoJPARepository = metodoPagoJPARepository;
    }


    public Optional<MetodoPagoDomainEntity> findById(String id){
        return metodoPagoJPARepository.findById(java.util.UUID.fromString(id))
            .map(MetodoPagoMapper::toDomainEntity);
    }

    public boolean save (MetodoPagoDomainEntity metodoPagoDomainEntity){
        try {
            metodoPagoJPARepository.save(
                MetodoPagoMapper.toEntity(metodoPagoDomainEntity)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<MetodoPagoDomainEntity> findAll(){
        return metodoPagoJPARepository.findAll()
            .stream()
            .map(MetodoPagoMapper::toDomainEntity)
            .collect(java.util.stream.Collectors.toList());
    }

    public void deleteById(String id){
        metodoPagoJPARepository.deleteById(java.util.UUID.fromString(id));
    }

}
