package com.zentry.sigea.module_asistencias.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.InscripcionJPARepository;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.repositories.SesionJPARepository;
import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_asistencias.infrastructure.database.mappers.AsistenciaMapper;
import com.zentry.sigea.module_asistencias.infrastructure.repositories.AsistenciaJPARepository;

@Repository
public class AsistenciaRepositoryAdapter implements IAsistenciaRepository {
    
    private final AsistenciaJPARepository asistenciaJPARepository;
    private final SesionJPARepository sesionJPARepository;
    private final InscripcionJPARepository inscripcionJPARepository;

    public AsistenciaRepositoryAdapter(
        AsistenciaJPARepository asistenciaJPARepository,
        SesionJPARepository sesionJPARepository,
        InscripcionJPARepository inscripcionJPARepository
    ){
        this.asistenciaJPARepository = asistenciaJPARepository;
        this.sesionJPARepository = sesionJPARepository;
        this.inscripcionJPARepository = inscripcionJPARepository;
    }

    @Override
    public void save(AsistenciaDomainEntity asistenciaDomainEntity){
        InscripcionEntity inscripcionEntity = inscripcionJPARepository.findById(
            UUID.fromString(asistenciaDomainEntity.getInscripcionId())
        ).orElseThrow(() -> new IllegalArgumentException(
            "No se encontró inscripción con ID: " + asistenciaDomainEntity.getInscripcionId()
        ));

        SesionEntity sesionEntity = sesionJPARepository.findById(
            UUID.fromString(asistenciaDomainEntity.getSesionId())
        ).orElseThrow(() -> new IllegalArgumentException(
            "No se encontró sesión con ID: " + asistenciaDomainEntity.getSesionId()
        ));

        asistenciaJPARepository.save(
            AsistenciaMapper.toEntity(
                asistenciaDomainEntity,
                sesionEntity,
                inscripcionEntity
            )
        );
    }

    public void saveAll(List<AsistenciaDomainEntity> listAsistenciaDomainEntities){
        asistenciaJPARepository.saveAll(
            listAsistenciaDomainEntities.stream()
                .map((ad) -> {
                    InscripcionEntity inscripcionEntity = inscripcionJPARepository.findById(
                        UUID.fromString(ad.getInscripcionId())
                    ).orElse(null);

                    SesionEntity sesionEntity = sesionJPARepository.findById(
                        UUID.fromString(ad.getSesionId())
                    ).orElse(null);

                    return AsistenciaMapper.toEntity(
                        ad, 
                        sesionEntity, 
                        inscripcionEntity
                    );
                }).collect(Collectors.toList())
        );
    }

    public Optional<AsistenciaDomainEntity> findById(String id){
        return asistenciaJPARepository.findById(UUID.fromString(id))
            .map(AsistenciaMapper::toDomain);
    }

    @Override
    public List<AsistenciaDomainEntity> findByInscripcionId(String inscripcionId){
        return asistenciaJPARepository.findByInscripcionId(UUID.fromString(inscripcionId))
            .stream()
            .map(AsistenciaMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AsistenciaDomainEntity> findBySesionId(String sesionId){
        return asistenciaJPARepository.findBySesionId(UUID.fromString(sesionId))
            .stream()
            .map(AsistenciaMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AsistenciaDomainEntity> findBySesionIdAndPresente(String sesionId, Boolean presente){
        return asistenciaJPARepository.findBySesionIdAndPresente(
            UUID.fromString(sesionId), presente
        )
            .stream()
            .map(AsistenciaMapper::toDomain)
            .collect(Collectors.toList());
    }

    public List<String> findIdsByInscripcionId(String inscripcionId){
        return asistenciaJPARepository.findIdsByInscripcionId(
            UUID.fromString(inscripcionId)
        ).stream()
        .map(Object::toString)
        .collect(Collectors.toList());
    }

    public boolean existsById(String id){
        return asistenciaJPARepository.existsById(UUID.fromString(id));
    }
}