package com.zentry.sigea.module_notificaciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.infrastructure.database.mappers.EstadoNotificacionMapper;
import com.zentry.sigea.module_notificaciones.infrastructure.repository.EstadoNotificacionJPARepository;

@Repository
public class EstadoNotificacionRepositoryAdapter implements IEstadoNotificacionRepository {
    
    private final EstadoNotificacionJPARepository estadoNotificacionJPARepository;

    public EstadoNotificacionRepositoryAdapter(EstadoNotificacionJPARepository estadoNotificacionJPARepository){
        this.estadoNotificacionJPARepository = estadoNotificacionJPARepository;
    }

    public Optional<EstadoNotificacionDomainEntity> findById(String id){
        return estadoNotificacionJPARepository.findById(UUID.fromString(id))
            .map(ei -> EstadoNotificacionMapper.toDomain(ei));
    }

    public Optional<EstadoNotificacionDomainEntity> findByCodigo(String codigo){
        return estadoNotificacionJPARepository.findByCodigo(codigo)
            .map(ei -> EstadoNotificacionMapper.toDomain(ei));
    }

    public List<EstadoNotificacionDomainEntity> findAll(){
        return estadoNotificacionJPARepository.findAll()
            .stream()
            .map(EstadoNotificacionMapper::toDomain)
            .collect(Collectors.toList());
    }

    public EstadoNotificacionDomainEntity save(EstadoNotificacionDomainEntity estadoNotificacionDomainEntity){
        var savedEntity = estadoNotificacionJPARepository.save(
            EstadoNotificacionMapper.toEntity(estadoNotificacionDomainEntity)
        );
        return EstadoNotificacionMapper.toDomain(savedEntity);
    }

    public void deleteById(String id){
        estadoNotificacionJPARepository.deleteById(UUID.fromString(id));
    }

    @Override
    public boolean existsById(String id) {
        return estadoNotificacionJPARepository.existsById(UUID.fromString(id));
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return estadoNotificacionJPARepository.findByCodigo(codigo).isPresent();
    }
}
