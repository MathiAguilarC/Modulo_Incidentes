package com.BackTesting.ModuloIncidencias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cliente;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    // getters y setters
    public Integer getId_cliente() {
        return id_cliente;
    }
    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }
}
