package com.zentry.sigea.module_pago.infrastucture.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentry.sigea.module_pago.infrastucture.database.entities.PagoEntity;

public interface  PagoJPARepository  extends JpaRepository<PagoEntity, UUID> {
    
}
