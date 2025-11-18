package com.zentry.sigea.module_asistencias.infrastructure.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "asistencia" , 
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_asistencia_por_sesion" , 
            columnNames = {"sesion_id" , "inscripcion_id"}
        )
    }
)
@Getter
@Setter
public class AsistenciaEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_asistencia" , updatable = false , nullable = false , 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionEntity sesion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private InscripcionEntity inscripcion;

    @Column(name = "presente", nullable = false)
    private Boolean presente = false;

    @Column(name = "registrado_en", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime registradoEn = LocalDateTime.now();

}
