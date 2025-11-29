package com.zentry.sigea.module_inscripciones.infrastructure.database.mappers;

import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

/**
 * Mapper para convertir entre Inscripcion (dominio) y InscripcionEntity (JPA)
 */
public class InscripcionMapper {

    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public static InscripcionEntity toEntity(
        InscripcionDomainEntity inscripcionDomainEntity,
        UsuarioEntity usuarioEntity,
        ActividadEntity actividadEntity
    ) {
        if (inscripcionDomainEntity == null) {
            return null;
        }

        InscripcionEntity inscripcionEntity = new InscripcionEntity();

        inscripcionEntity.setFechaInscripcion(inscripcionDomainEntity.getFechaInscripcion());
        inscripcionEntity.setUsuario(usuarioEntity);
        inscripcionEntity.setActividad(actividadEntity);
        inscripcionEntity.setEstadoInscripcion(
            EstadoInscripcionMapper.toEntity(
                inscripcionDomainEntity.getEstadoInscripcionDomainEntity()
            )
        );

        return inscripcionEntity;
    }

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public static InscripcionDomainEntity toDomain(InscripcionEntity inscripcionEntity) {
        if (inscripcionEntity == null) {
            return null;
        }

        InscripcionDomainEntity inscripcionDomainEntity = new InscripcionDomainEntity();

        inscripcionDomainEntity.setFechaInscripcion(inscripcionEntity.getFechaInscripcion());
        inscripcionDomainEntity.setUsuarioId(
            inscripcionEntity.getUsuario() != null ? 
            inscripcionEntity.getUsuario().getId().toString() : null
        );
        inscripcionDomainEntity.setActividadId(
            inscripcionEntity.getActividad() != null ? 
            inscripcionEntity.getActividad().getId().toString() : null
        );
        inscripcionDomainEntity.setEstadoInscripcionDomainEntity(
            EstadoInscripcionMapper.toDomain(
                inscripcionEntity.getEstadoInscripcion()
            )
        );

        return inscripcionDomainEntity;
    }
}

