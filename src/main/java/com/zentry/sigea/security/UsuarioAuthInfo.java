package com.zentry.sigea.security;

public class UsuarioAuthInfo {
    private String id;
    private String correo;

    public UsuarioAuthInfo(
        String id , 
        String correo
    ){
        this.id = id;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }
    public String getCorreo() {
        return correo;
    }
}
