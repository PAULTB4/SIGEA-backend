package com.zentry.sigea.module_informe.infrastructure.database.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;

@Entity
@Table(name = "informe")
@Getter
@Setter
public class InformeEntity {

    @Id
    @GeneratedValue
    @Column(name = "id_informe", updatable = false, nullable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private ActividadEntity actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_informe_id", nullable = false)
    private TipoInformeEntity tipoInforme;

    @Column(name = "fecha_subida", nullable = false, columnDefinition = "DATE DEFAULT current_date")
    private LocalDate fechaSubida;

    @Column(name = "archivo_url", length = 300, nullable = true)
    private String archivoUrl;

    @Column(name = "observaciones", nullable = true, length = 500, columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "created_at", nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt= LocalDateTime.now();

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt=LocalDateTime.now();
}
