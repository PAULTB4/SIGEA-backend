package com.zentry.sigea.module_pago.infrastucture.repository;

import com.zentry.sigea.module_pago.infrastucture.database.entities.EstadoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface EstadoPagoRepository extends JpaRepository<EstadoPagoEntity, UUID> {
    Optional<EstadoPagoEntity> findByEtiqueta(String etiqueta);
}
