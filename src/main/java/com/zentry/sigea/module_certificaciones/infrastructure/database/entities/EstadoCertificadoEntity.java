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
@Table(name = "estado_certificado")
@Getter
@Setter
public class EstadoCertificadoEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_estado_certificado", updatable= false, nullable = false,
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID idEstadoCertificado;
    
    @Column(name = "codigo", length = 30, nullable = false, unique = true)
    private String codigo; // EMITIDO | REVOCADO | SUSPENDIDO
    
    @Column(name = "etiqueta", length = 60)
    private String etiqueta;
    
    // Constructores
}