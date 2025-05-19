package com.BackTesting.ModuloIncidencias.dto;

public class LoginResponse {
    private String tipoUsuario; // cliente o soporte
    private String rol;         // solo para soporte: soporte o administrador
    private String mensaje;
    // constructor, getters, setters

    public LoginResponse() {
    }

    public LoginResponse(String tipoUsuario, String rol, String mensaje) {
        this.tipoUsuario = tipoUsuario;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}