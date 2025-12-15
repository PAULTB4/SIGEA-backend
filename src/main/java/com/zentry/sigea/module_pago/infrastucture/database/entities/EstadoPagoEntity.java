package com.zentry.sigea.module_pago.infrastucture.database.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estado_pago")
@Getter
@Setter
public class EstadoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_estado")
    private UUID idEstadoPago;

    @Column(name = "codigo", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "etiqueta", nullable = false, length = 100)
    private String etiqueta;

}