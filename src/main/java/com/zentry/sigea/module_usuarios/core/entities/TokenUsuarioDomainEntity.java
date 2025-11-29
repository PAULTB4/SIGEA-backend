package com.zentry.sigea.module_usuarios.core.entities;

import java.time.Instant;

public class TokenUsuarioDomainEntity {
    private String id;
    private String token;
    private String usuarioId;
    private Instant expiryDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
