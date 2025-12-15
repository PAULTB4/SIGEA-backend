package com.zentry.sigea.module_pago.infrastucture.database.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "metodo_pago")
@Getter
@Setter
public class MetodoPagoEntity {

    @Id
    @GeneratedValue
    @Column(
        name = "id_metodo" , updatable = false , nullable = false , 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID idMetodoPago;

    @Column(name = "codigo", nullable = false, length = 100 , unique = true)
    private String descripcion;
    @Column(name = "etiqueta", nullable = false, length = 100)
    private String etiqueta;

}