package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "validacion",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_validacion_tipo_por_cert", 
            columnNames = {"certificado_id" , "tipo_validador_id"} 
        )
    }
)
@Getter
@Setter
public class ValidacionEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_validacion", updatable= false, nullable = false, 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID idValidacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificado_id", nullable = false)
    private CertificadoEntity certificado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_validador_id", nullable = false)
    private TipoValidadorEntity tipoValidador;
    
    @Column(name = "fecha_validacion", nullable = false)
    private LocalDate fechaValidacion;
    
    @Column(name = "resultado", length = 20, nullable = false)
    private String resultado = "APROBADO"; // APROBADO | RECHAZADO
    
    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;
    @PrePersist
    protected void onCreate() {
        fechaValidacion = LocalDate.now();
    }
    
    

}