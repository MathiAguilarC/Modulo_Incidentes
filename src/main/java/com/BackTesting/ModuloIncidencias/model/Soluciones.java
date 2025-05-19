package com.BackTesting.ModuloIncidencias.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Soluciones")
public class Soluciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolucion;

    @ManyToOne
    @JoinColumn(name = "id_incidencia")
    private Incidencia incidencia;

    @Column(nullable = false)
    private String tipoSolucion; // temporal o definitiva

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaAplicacion;

    @ManyToOne
    @JoinColumn(name = "id_empleado_solucion")
    private EmpleadoSoporte empleadoSolucion;


    // Getters y setters

    public EmpleadoSoporte getEmpleadoSolucion() {
        return empleadoSolucion;
    }

    public void setEmpleadoSolucion(EmpleadoSoporte empleadoSolucion) {
        this.empleadoSolucion = empleadoSolucion;
    }

    public LocalDateTime getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDateTime fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoSolucion() {
        return tipoSolucion;
    }

    public void setTipoSolucion(String tipoSolucion) {
        this.tipoSolucion = tipoSolucion;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public Integer getIdSolucion() {
        return idSolucion;
    }

    public void setIdSolucion(Integer idSolucion) {
        this.idSolucion = idSolucion;
    }
}
