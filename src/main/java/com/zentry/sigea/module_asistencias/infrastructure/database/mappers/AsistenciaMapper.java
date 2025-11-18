package com.zentry.sigea.module_asistencias.infrastructure.database.mappers;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;
import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.infrastructure.database.entities.AsistenciaEntity;

public class AsistenciaMapper {
    
    public static AsistenciaEntity toEntity(
        AsistenciaDomainEntity asistenciaDomainEntity,
        SesionEntity sesionEntity,
        InscripcionEntity inscripcionEntity
    ){
        AsistenciaEntity asistenciaEntity = new AsistenciaEntity();

        asistenciaEntity.setSesion(sesionEntity);
        asistenciaEntity.setInscripcion(inscripcionEntity);
        asistenciaEntity.setPresente(asistenciaDomainEntity.getPresente());
        asistenciaEntity.setRegistradoEn(asistenciaDomainEntity.getRegistradoEn());

        return asistenciaEntity;
    }

    public static AsistenciaDomainEntity toDomain(AsistenciaEntity asistenciaEntity){
        return AsistenciaDomainEntity.reconstruct(
            asistenciaEntity.getId().toString(),
            asistenciaEntity.getSesion().getId().toString(),
            asistenciaEntity.getInscripcion().getId().toString(),
            asistenciaEntity.getPresente(),
            asistenciaEntity.getRegistradoEn()
        );
    }
}