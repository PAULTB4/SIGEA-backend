package com.zentry.sigea.module_pago.infrastucture.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zentry.sigea.module_pago.infrastucture.database.entities.PagoEntity;

public interface PagoJPARepository extends JpaRepository<PagoEntity, UUID> {

    @Query("SELECT p FROM PagoEntity p JOIN FETCH p.inscripcion i WHERE i.id = :inscripcionId")
    boolean existsByInscripcionId(UUID inscripcionId);

}
