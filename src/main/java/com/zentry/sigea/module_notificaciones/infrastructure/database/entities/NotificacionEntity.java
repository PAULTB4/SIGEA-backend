package com.zentry.sigea.module_notificaciones.infrastructure.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

@Entity
@Table(name = "notificacion")
@Getter
@Setter
public class NotificacionEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_notificacion", 
        updatable = false, 
        nullable = false, 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = true)
    private ActividadEntity actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_notificacion_id", nullable = false)
    private TipoNotificacionEntity tipoNotificacion;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_notificacion_id", nullable = false)
    private EstadoNotificacionEntity estadoNotificacion;

    @Column(name = "canal", nullable = false, length = 20)
    private String canal; // Almacena el valor del enum: SISTEMA, CORREO, WHATSAPP

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
