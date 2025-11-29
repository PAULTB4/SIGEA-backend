package com.zentry.sigea.module_inscripciones.infrastructure.database.entities;

import java.time.LocalDate;
import java.util.UUID;

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

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

@Entity
@Table(
    name = "inscripcion",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_inscripcion_unica",
            columnNames = {"usuario_id", "actividad_id"}
        )
    }
)
@Getter
@Setter
public class InscripcionEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_inscripcion", 
        updatable = false, 
        nullable = false, 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @Column(name = "fecha_inscripcion", nullable = false, columnDefinition = "DATE")
    private LocalDate fechaInscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private ActividadEntity actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_inscripcion_id", nullable = false)
    private EstadoInscripcionEntity estadoInscripcion;
}

