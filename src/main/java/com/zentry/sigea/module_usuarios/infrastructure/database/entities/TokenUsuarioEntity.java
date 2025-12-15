package com.zentry.sigea.module_usuarios.infrastructure.database.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

@Entity
@Table(
    name = "token_usuario"
)
@Getter
@Setter
public class TokenUsuarioEntity {
    
    @Id
    @GeneratedValue
    @Column(
        name = "id_token_usuario" , updatable = false , nullable = false , 
        columnDefinition = "UUID DEFAULT gen_random_uuid()"
    )
    private UUID id;

    @Column(name = "token" , nullable = false , unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsuarioEntity usuario;

    @Column(name = "expiry_date" , columnDefinition = "TIMESTAMP", nullable = false)
    private Instant expiryDate;
}
