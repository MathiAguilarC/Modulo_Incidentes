package com.BackTesting.ModuloIncidencias.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_reporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    private String titulo;

    private String descripcion;

    private String tipo_error;  // pago, devolucion, producto, otro

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte;

    // Getters y setters

    public Integer getId_reporte() {
        return id_reporte;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo_error() {
        return tipo_error;
    }

    public void setTipo_error(String tipo_error) {
        this.tipo_error = tipo_error;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
}
