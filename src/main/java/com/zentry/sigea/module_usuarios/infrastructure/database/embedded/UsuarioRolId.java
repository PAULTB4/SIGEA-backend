package com.zentry.sigea.module_usuarios.infrastructure.database.embedded;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioRolId implements Serializable {

    // SIEMPRE COLOCA EL NOMBRE DE LA COLUMNA SEGUN LA TABLA A LA CUAL IRA EL EMBEDDED
    // En mi caso los nombres se basan en los que hay en UsuarioRolEntity.
    @Column(name = "usuario_id")
    private UUID idUsuario;

    @Column(name = "rol_id")
    private UUID idRol;
    
    // SIEMPRE COLOCA UN CONSTRUCTOR VACIO Y UNO CON PARAMETROS DE FORMA MANUAL
    public UsuarioRolId() {}

    public UsuarioRolId(UUID idUsuario, UUID idRol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof UsuarioRolId that)) return false;
        return idUsuario.equals(that.idUsuario) && idRol.equals(that.idRol);
    }

    @Override
    public int hashCode(){
        return java.util.Objects.hash(idUsuario , idRol);
    }

    public UUID getIdRol() {
        return idRol;
    }
    public void setIdRol(UUID idRol) {
        this.idRol = idRol;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }
}
