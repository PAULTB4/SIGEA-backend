package com.zentry.sigea.module_inscripciones.infrastructure.database.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.id.uuid.UuidValueGenerator;
import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_inscripciones.infrastructure.database.mappers.InscripcionMapper;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.InscripcionJPARepository;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@Repository
public class InscripcionRepositoryAdapter implements IInscripcionRepository {
    
    private final InscripcionJPARepository inscripcionJPARepository;
    private final UsuarioJPARepository usuarioJPARepository;
    private final ActividadJPARepository actividadJPARepository;

    public InscripcionRepositoryAdapter(
        InscripcionJPARepository inscripcionJPARepository,
        UsuarioJPARepository usuarioJPARepository,
        ActividadJPARepository actividadJPARepository
    ) {
        this.inscripcionJPARepository = inscripcionJPARepository;
        this.usuarioJPARepository = usuarioJPARepository;
        this.actividadJPARepository = actividadJPARepository;
    }

    public boolean save(InscripcionDomainEntity inscripcionDomainEntity) {
        try {
            UsuarioEntity usuarioEntity = usuarioJPARepository.findById(
                UUID.fromString(inscripcionDomainEntity.getUsuarioId())
            ).orElse(null);

            ActividadEntity actividadEntity = actividadJPARepository.findById(
                UUID.fromString(inscripcionDomainEntity.getActividadId())
            ).orElse(null);

            if (usuarioEntity == null || actividadEntity == null) {
                return false;
            }

            inscripcionJPARepository.save(
                InscripcionMapper.toEntity(
                    inscripcionDomainEntity,
                    usuarioEntity,
                    actividadEntity
                )
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<InscripcionDomainEntity> findById(String id) {
        return inscripcionJPARepository.findById(
            UUID.fromString(id)
        ).map(InscripcionMapper::toDomain);
    }

    public List<InscripcionDomainEntity> findByUsuarioId(String usuarioId) {
        return inscripcionJPARepository.findByUsuarioId(
            UUID.fromString(usuarioId)
        ).stream()
        .map(InscripcionMapper::toDomain)
        .collect(Collectors.toList());
    }

    public List<InscripcionDomainEntity> findByActividadId(String actividadId) {
        return inscripcionJPARepository.findByActividadId(
            UUID.fromString(actividadId)
        ).stream()
        .map(InscripcionMapper::toDomain)
        .collect(Collectors.toList());
    }

    public List<String> findIdByListActividadIds(List<String> listActividadIds){
        return inscripcionJPARepository.findIdByListActividadIds(
            listActividadIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList())
        ).stream()
        .map(Object::toString)
        .collect(Collectors.toList());
    }

    public List<InscripcionDomainEntity> findAll() {
        return inscripcionJPARepository.findAll().stream()
            .map(InscripcionMapper::toDomain)
            .collect(Collectors.toList());
    }

    public Optional<InscripcionDomainEntity> findByUsuarioIdAndActividadId(String usuarioId, String actividadId) {
        return inscripcionJPARepository.findByUsuarioIdAndActividadId(
            UUID.fromString(usuarioId),
            UUID.fromString(actividadId)
        ).map(InscripcionMapper::toDomain);
    }

    public List<InscripcionDomainEntity> findByEstadoInscripcionId(String estadoId) {
        return inscripcionJPARepository.findByEstadoInscripcionId(
            UUID.fromString(estadoId)
        ).stream()
        .map(InscripcionMapper::toDomain)
        .collect(Collectors.toList());
    }

    public boolean existsByUsuarioIdAndActividadId(String usuarioId, String actividadId) {
        return inscripcionJPARepository.existsByUsuarioIdAndActividadId(
            UUID.fromString(usuarioId),
            UUID.fromString(actividadId)
        );
    }

    public String findIdByUsuarioIdAndActividadId(String usuarioId , String actividadId){
        return inscripcionJPARepository.findIdByUsuarioIdAndActividadId(
            UUID.fromString(usuarioId),
            UUID.fromString(actividadId) 
        ).toString();
    }

    public boolean existsById(String id) {
        return inscripcionJPARepository.existsById(UUID.fromString(id));
    }

    public void deleteById(String id) {
        inscripcionJPARepository.deleteById(UUID.fromString(id));
    }
}

