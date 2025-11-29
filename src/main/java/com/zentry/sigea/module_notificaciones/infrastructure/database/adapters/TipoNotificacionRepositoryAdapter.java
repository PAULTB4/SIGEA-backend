package com.zentry.sigea.module_notificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.ITipoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.infrastructure.database.mappers.TipoNotificacionMapper;
import com.zentry.sigea.module_notificaciones.infrastructure.repository.TipoNotificacionJPARepository;

@Repository
public class TipoNotificacionRepositoryAdapter implements ITipoNotificacionRepository {
    
    private final TipoNotificacionJPARepository tipoNotificacionJPARepository;

    public TipoNotificacionRepositoryAdapter(TipoNotificacionJPARepository tipoNotificacionJPARepository){
        this.tipoNotificacionJPARepository = tipoNotificacionJPARepository;
    }

    public Optional<TipoNotificacionDomainEntity> findById(String id){
        return tipoNotificacionJPARepository.findById(UUID.fromString(id))
            .map(ti -> TipoNotificacionMapper.toDomain(ti));
    }

    public Optional<TipoNotificacionDomainEntity> findByCodigo(String codigo){
        return tipoNotificacionJPARepository.findByCodigo(codigo)
            .map(ti -> TipoNotificacionMapper.toDomain(ti));
    }

    public List<TipoNotificacionDomainEntity> findAll(){
        return tipoNotificacionJPARepository.findAll()
            .stream()
            .map(TipoNotificacionMapper::toDomain)
            .collect(Collectors.toList());
    }

    public TipoNotificacionDomainEntity save(TipoNotificacionDomainEntity tipoNotificacionDomainEntity){
        var savedEntity = tipoNotificacionJPARepository.save(
            TipoNotificacionMapper.toEntity(tipoNotificacionDomainEntity)
        );
        return TipoNotificacionMapper.toDomain(savedEntity);
    }

    public void deleteById(String id){
        tipoNotificacionJPARepository.deleteById(UUID.fromString(id));
    }

    @Override
    public boolean existsById(String id) {
        return tipoNotificacionJPARepository.existsById(UUID.fromString(id));
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return tipoNotificacionJPARepository.findByCodigo(codigo).isPresent();
    }
}
