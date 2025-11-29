package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_validador")
@Getter
@Setter
public class TipoValidadorEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_tipo_validador", updatable= false, nullable = false,
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID idTipoValidador;
    
    @Column(name = "codigo", length = 30, nullable = false, unique = true)
    private String codigo; // QR | HASH | URL_PUBLICA | OCSP
    
    @Column(name = "etiqueta", length = 60)
    private String etiqueta;
    
    
}