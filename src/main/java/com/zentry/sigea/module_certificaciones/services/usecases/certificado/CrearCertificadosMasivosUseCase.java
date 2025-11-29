package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent.EstadoCertificado;

@Component
public class CrearCertificadosMasivosUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CrearCertificadoUseCase.class);

    private final ICertificadoRepository certificadoRepository;
    private final IEstadoCertificadoRepository estadoCertificadoRepository;
    private final IAsistenciaRepository asistenciaRepository;
    private final IInscripcionRepository inscripcionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CrearCertificadosMasivosUseCase(
        ICertificadoRepository certificadoRepository,
        IEstadoCertificadoRepository estadoCertificadoRepository,
        IAsistenciaRepository asistenciaRepository,
        IInscripcionRepository inscripcionRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.certificadoRepository = certificadoRepository;
        this.estadoCertificadoRepository = estadoCertificadoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Map<String , Boolean> execute(List<String> listAsistenciaIds){

        Map<String , Boolean> response = new HashMap<>();

        for(String asistenciaId : listAsistenciaIds){

            if(certificadoRepository.existsByAsistenciaId(asistenciaId)){
                response.put(asistenciaId, true);
                continue;
            }

            EstadoCertificadoDomainEntity estadoCertificadoDomainEntity = estadoCertificadoRepository
                .findByCodigo("EMITIDO")
                .orElseThrow(() -> new IllegalStateException());

            String codigoValidacion = generarCodigoValidacion();

            while (certificadoRepository.existsByCodigoValidacion(codigoValidacion)) {
                codigoValidacion = generarCodigoValidacion();
            }

            CertificadoDomainEntity certificadoDomainEntity = CertificadoDomainEntity.create(
                asistenciaId, 
                codigoValidacion, 
                estadoCertificadoDomainEntity
            );

            try {
                CertificadoDomainEntity certificadoGuardado = certificadoRepository.save(certificadoDomainEntity);

                try {
                    AsistenciaDomainEntity asistenciaDomainEntity = asistenciaRepository.findById(asistenciaId)
                        .orElseThrow(() -> new IllegalArgumentException());

                    InscripcionDomainEntity inscripcionDomainEntity = inscripcionRepository.findById(asistenciaDomainEntity.getInscripcionId())
                        .orElseThrow(() -> new IllegalArgumentException());
                    
                    logger.info("Publicando evento CertificadoGeneradoEvent para certificado: {}", certificadoGuardado.getIdCertificado());

                    eventPublisher.publishEvent(
                        new CertificadoGeneradoEvent(
                            inscripcionDomainEntity.getUsuarioId(), 
                            certificadoGuardado.getIdCertificado(), 
                            inscripcionDomainEntity.getActividadId(),
                            "Actividad Completada", 
                            certificadoGuardado.getCodigoValidacion(), 
                            certificadoGuardado.getFechaEmision(), 
                            EstadoCertificado.EMITIDO, 
                            certificadoGuardado.getUrlPdf(), 
                            asistenciaId.toString(), 
                            LocalDateTime.now()                    
                        )
                    );

                    response.put(asistenciaId, true);

                    logger.info("Evento CertificadoGeneradoEvent publicado exitosamente");

                } catch (Exception e) {
                    logger.error("Error publicando evento de notificación para certificado {}: {}", 
                        certificadoGuardado.getIdCertificado(), e.getMessage(), e);
                }

            } catch (Exception e) {
                response.put(asistenciaId, false);
                continue;
            }
        }
        
        return response;
    }

    public String generarCodigoValidacion(){
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
