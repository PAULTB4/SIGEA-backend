package com.zentry.sigea.module_pago.infrastucture.database.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.InscripcionJPARepository;
import com.zentry.sigea.module_pago.core.entities.PagoDomainEntity;
import com.zentry.sigea.module_pago.core.repository.IPagoRepository;
import com.zentry.sigea.module_pago.infrastucture.database.entities.EstadoPagoEntity;
import com.zentry.sigea.module_pago.infrastucture.database.entities.MetodoPagoEntity;
import com.zentry.sigea.module_pago.infrastucture.database.mappers.PagoMapper;
import com.zentry.sigea.module_pago.infrastucture.repository.EstadoPagoJPARepository;
import com.zentry.sigea.module_pago.infrastucture.repository.MetodoPagoJPARepository;
import com.zentry.sigea.module_pago.infrastucture.repository.PagoJPARepository;

@Repository
public class PagoRepositoryAdapter implements IPagoRepository {

    private final PagoJPARepository pagoRepository;
    private final InscripcionJPARepository inscripcionJPARepository;
    private final MetodoPagoJPARepository metodoPagoJPARepository;
    private final EstadoPagoJPARepository estadoPagoJPARepository;

    public PagoRepositoryAdapter(
        PagoJPARepository pagoRepository,
        InscripcionJPARepository inscripcionJPARepository,
        MetodoPagoJPARepository metodoPagoJPARepository,
        EstadoPagoJPARepository estadoPagoJPARepository
    ) {
        this.pagoRepository = pagoRepository;
        this.inscripcionJPARepository = inscripcionJPARepository;
        this.metodoPagoJPARepository = metodoPagoJPARepository;
        this.estadoPagoJPARepository = estadoPagoJPARepository;
    }

    public boolean save(PagoDomainEntity pagoEntity) {
        try {
            /*
                * Obtener las entidades relacionadas necesarias 
                * para crear la entidad PagoEntity
                * 
            */
            Optional<InscripcionEntity> inscripcionEntity = inscripcionJPARepository.findById(
                UUID.fromString(pagoEntity.getInscripcionId())
            );
            if (inscripcionEntity.isEmpty()) {
                return false;
            }


            Optional<EstadoPagoEntity>estadoPagoEntity = estadoPagoJPARepository.findById(
                UUID.fromString(pagoEntity.getEstadoId())
            );  

            if (estadoPagoEntity.isEmpty()) {
                return false;
            }


            Optional<MetodoPagoEntity> metodoPagoEntity = metodoPagoJPARepository.findById(
                UUID.fromString(pagoEntity.getMetodoId())
            );
            if (metodoPagoEntity.isEmpty()) {
                return false;
            }


            pagoRepository.save(
                PagoMapper.toEntity(
                    pagoEntity,
                    inscripcionEntity.get(),
                    metodoPagoEntity.get(),
                    estadoPagoEntity.get()
                )
            );

            return true;

        } catch (Exception e) {
            return false;
        }
    }
    
}   
