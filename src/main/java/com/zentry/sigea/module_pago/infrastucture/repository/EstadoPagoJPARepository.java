package com.zentry.sigea.module_pago.infrastucture.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentry.sigea.module_pago.infrastucture.database.entities.EstadoPagoEntity;


public  interface  EstadoPagoJPARepository  extends  JpaRepository<EstadoPagoEntity, UUID>{

}