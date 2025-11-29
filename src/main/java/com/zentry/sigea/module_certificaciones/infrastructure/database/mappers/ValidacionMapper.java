package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import org.springframework.stereotype.Component;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.CertificadoEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.ValidacionEntity;

/**
 * Mapper para convertir entre entidades de dominio e infraestructura de Validacion
 */
@Component
public class ValidacionMapper {

    private final CertificadoMapper certificadoMapper;
    private final TipoValidadorMapper tipoValidadorMapper;

    public ValidacionMapper(CertificadoMapper certificadoMapper, TipoValidadorMapper tipoValidadorMapper) {
        this.certificadoMapper = certificadoMapper;
        this.tipoValidadorMapper = tipoValidadorMapper;
    }

    /**
     * Convierte de entidad de infraestructura a entidad de dominio
     */
    public ValidacionDomainEntity toDomain(ValidacionEntity validacion) {
        if (validacion == null) {
            return null;
        }

        ValidacionDomainEntity domainEntity = new ValidacionDomainEntity();
        domainEntity.setCertificado(validacion.getCertificado().getIdCertificado().toString());
        domainEntity.setTipoValidador(validacion.getTipoValidador().getCodigo());
        domainEntity.setFechaValidacion(validacion.getFechaValidacion());
        domainEntity.setResultado(validacion.getResultado());
        domainEntity.setDetalle(validacion.getDetalle());

        return domainEntity;
    }

    /**
     * Convierte de entidad de dominio a entidad de infraestructura
     */
    public ValidacionEntity toInfrastructure(
        ValidacionDomainEntity domainEntity , 
        CertificadoEntity certificadoEntity , 
        TipoValidadorEntity tipoValidadorEntity
    ) {
        if (domainEntity == null) {
            return null;
        }

        ValidacionEntity validacion = new ValidacionEntity();
        validacion.setCertificado(certificadoEntity);
        validacion.setTipoValidador(tipoValidadorEntity);
        validacion.setFechaValidacion(domainEntity.getFechaValidacion());
        validacion.setResultado(domainEntity.getResultado());
        validacion.setDetalle(domainEntity.getDetalle());

        return validacion;
    }

    /**
     * Actualiza una entidad de infraestructura con los datos de dominio
     */
    public void updateInfrastructure(ValidacionEntity validacion, ValidacionDomainEntity domainEntity) {
        if (validacion == null || domainEntity == null) {
            return;
        }

        // No actualizamos certificado y tipoValidador ya que son claves de relación
        validacion.setFechaValidacion(domainEntity.getFechaValidacion());
        validacion.setResultado(domainEntity.getResultado());
        validacion.setDetalle(domainEntity.getDetalle());
    }
}