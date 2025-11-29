package com.zentry.sigea.module_actividad.infrastructure.database.adapters;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import com.zentry.sigea.module_actividad.infrastructure.database.mappers.ActividadMapper;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@Repository
public class ActividadRepositoryAdapter implements IActividadRespository {

    private final ActividadJPARepository actividadJPARepository;
    private final UsuarioJPARepository usuarioJPARepository;

    public ActividadRepositoryAdapter(
            ActividadJPARepository actividadJPARepository,
            UsuarioJPARepository usuarioJPARepository) {
        this.actividadJPARepository = actividadJPARepository;
        this.usuarioJPARepository = usuarioJPARepository;
    }

    public boolean save(ActividadDomainEntity actividadDomainEntity) {
        try {
            UsuarioEntity usuarioEntity = usuarioJPARepository.findById(
                    UUID.fromString(actividadDomainEntity.getOrganizadorId())).orElse(null);

            actividadJPARepository.save(
                    ActividadMapper.toEntity(
                            actividadDomainEntity,
                            usuarioEntity));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<ActividadDomainEntity> findById(String id) {
        return actividadJPARepository.findById(
                UUID.fromString(id)).map(a -> ActividadMapper.toDomain(a));
    }

    public List<ActividadDomainEntity> findByOrganizadorId(String organizadorId) {
        return actividadJPARepository.findByOrganizadorId(
                UUID.fromString(organizadorId)).stream()
                .map(ActividadMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<ActividadDomainEntity> findAll() {
        return actividadJPARepository.findAll().stream()
                .map(ActividadMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<String> findAllIds(){
        return actividadJPARepository.findAllIds().stream()
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    public List<ActividadDomainEntity> findByEstadoActividadId(String statusId) {
        return actividadJPARepository.findByEstadoActividadId(UUID.fromString(statusId)).stream()
                .map(ActividadMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<ActividadDomainEntity> findByTipoActividadId(String tipoActividadId) {
        return actividadJPARepository.findByTipoActividadId(UUID.fromString(tipoActividadId)).stream()
                .map(ActividadMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<ActividadDomainEntity> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return actividadJPARepository.findByFechaInicioBetween(startDate, endDate)
                .stream()
                .map(ActividadMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<ActividadDomainEntity> findActiveActivities(String codigo) {
        return actividadJPARepository.findByEstadoActividad_Codigo(codigo)
                .stream()
                .map(ActividadMapper::toDomain)
                .collect(Collectors.toList());
    }

    public boolean existsById(String id) {
        return actividadJPARepository.existsById(UUID.fromString(id));
    }

    public void deleteById(String id) {
        actividadJPARepository.deleteById(UUID.fromString(id));
    }
}
