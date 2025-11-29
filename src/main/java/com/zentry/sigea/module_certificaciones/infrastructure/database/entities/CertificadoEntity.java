package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "certificado" ,
       uniqueConstraints = {
           @UniqueConstraint(
                    name = "ux_asistencia_unica", 
                    columnNames = {"asistencia_id"}
                  )
       })
@Getter
@Setter
public class CertificadoEntity {
    
    @Id
    @GeneratedValue
    @Column(name = "id_certificado" , updatable= false, nullable = false,
            columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID idCertificado;
    
    // Cambiado de inscripcion_id para evitar dependencia de asistencia
    @Column(name = "asistencia_id", nullable = false, unique = true)
    private UUID asistenciaId;
    
    @Column(name = "codigo_validacion", length = 50, nullable = false, unique = true)
    private String codigoValidacion;
    
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoCertificadoEntity estado;
    
    @Column(name = "url_pdf", length = 300)
    private String urlPdf;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        fechaEmision = LocalDate.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}