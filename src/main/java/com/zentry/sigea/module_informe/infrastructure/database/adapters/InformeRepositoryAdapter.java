package com.zentry.sigea.module_informe.infrastructure.database.adapters;

import java.util.Optional;
import java.util.UUID;

import com.zentry.sigea.module_informe.core.entities.InformeDomainEntity;
import com.zentry.sigea.module_informe.core.repositories.IInformeRepository;

import com.zentry.sigea.module_informe.infrastructure.database.entities.InformeEntity;
import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;
import com.zentry.sigea.module_informe.infrastructure.database.mappers.InformeMapper;

import com.zentry.sigea.module_informe.infrastructure.repository.InformeJPARepository;
import com.zentry.sigea.module_informe.infrastructure.repository.TipoInformeJPARepository;

import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;


import org.springframework.stereotype.Repository;


@Repository
public class InformeRepositoryAdapter implements IInformeRepository {

    private final InformeJPARepository informeJPARepository;
    private final ActividadJPARepository actividadJPARepository;
    private final TipoInformeJPARepository tipoInformeJPARepository;

    public InformeRepositoryAdapter(
        InformeJPARepository informeJPARepository,
        ActividadJPARepository actividadJPARepository,
        TipoInformeJPARepository tipoInformeJPARepository
    ) {
        this.informeJPARepository = informeJPARepository;
        this.actividadJPARepository = actividadJPARepository;
        this.tipoInformeJPARepository = tipoInformeJPARepository;
    }

    @Override
    public InformeDomainEntity save(InformeDomainEntity informe) {
        // Buscar entidades relacionadas
        UUID actividadId = UUID.fromString(informe.getActividadId());
        UUID tipoInformeId = UUID.fromString(informe.getTipoInformeId());

        ActividadEntity actividadEntity = actividadJPARepository.findById(actividadId)
            .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada"));
        TipoInformeEntity tipoInformeEntity = tipoInformeJPARepository.findById(tipoInformeId)
            .orElseThrow(() -> new IllegalArgumentException("Tipo de informe no encontrado"));

        InformeEntity entity = InformeMapper.toEntity(informe, actividadEntity, tipoInformeEntity);
        InformeEntity saved = informeJPARepository.save(entity);
        return InformeMapper.toDomain(saved);
    }

    @Override
    public Optional<InformeDomainEntity> findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return informeJPARepository.findById(uuid)
                    .map(InformeMapper::toDomain);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<InformeDomainEntity> findById(UUID id) {
        return findById(id.toString());
    }

    @Override
    public java.util.List<InformeDomainEntity> findAll() {
        return informeJPARepository.findAll()
            .stream()
            .map(InformeMapper::toDomain)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            informeJPARepository.deleteById(uuid);
        } catch (Exception e) {
            // Manejo de error si el ID no es válido
        }
    }

    @Override
    public void deleteById(UUID id) {
        deleteById(id.toString());
    }

    @Override
    public boolean existsById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return informeJPARepository.existsById(uuid);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return existsById(id.toString());
    }
}