package com.zentry.sigea.module_informe.infrastructure.database.adapters;

import com.zentry.sigea.module_informe.core.entities.TipoInformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.ITipoInformeRepository;
import com.zentry.sigea.module_informe.infrastructure.repository.TipoInformeJPARepository;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.TipoInformeMapper;
import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class TipoInformeRepositoryAdapter implements ITipoInformeRepository {

    private final TipoInformeJPARepository tipoInformeJPARepository;

    public TipoInformeRepositoryAdapter(TipoInformeJPARepository tipoInformeJPARepository) {
        this.tipoInformeJPARepository = tipoInformeJPARepository;
    }

    @Override
    public Optional<TipoInformeDomainEntity> findById(String id) {
        return tipoInformeJPARepository.findById(UUID.fromString(id))
            .map(TipoInformeMapper::toDomain);
    }

    @Override
    public List<TipoInformeDomainEntity> findAll() {
        return tipoInformeJPARepository.findAll()
            .stream()
            .map(TipoInformeMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public TipoInformeDomainEntity save(TipoInformeDomainEntity tipoInforme) {
        TipoInformeEntity entity = TipoInformeMapper.toEntity(tipoInforme);
        TipoInformeEntity savedEntity = tipoInformeJPARepository.save(entity);
        return TipoInformeMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        tipoInformeJPARepository.deleteById(UUID.fromString(id));
    }

    @Override
    public Optional<TipoInformeDomainEntity> findByCodigo(String codigo) {
        return tipoInformeJPARepository.findByCodigo(codigo)
            .map(TipoInformeMapper::toDomain);
    }
}
