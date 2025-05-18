package com.BackTesting.ModuloIncidencias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "empleadosoporte")
public class EmpleadoSoporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_empleado;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private String rol;  // 'soporte' o 'administrador'

    // getters y setters

    public Integer getId_empleado() {
        return id_empleado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
