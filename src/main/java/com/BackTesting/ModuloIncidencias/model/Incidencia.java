package com.BackTesting.ModuloIncidencias.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Incidencia")
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidencia")
    private Integer idIncidencia;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reporte")
    private Reporte reporte;

    private String estado;  // Pendiente, En Progreso, Resuelto, Cerrado

    private String prioridad; // Alta, Media, Baja

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(length = 255)
    private String comentario;

    @Column(name = "fecha_solucion_temporal")
    private LocalDateTime fechaSolucionTemporal;

    @Column(name = "fecha_solucion_definitiva")
    private LocalDateTime fechaSolucionDefinitiva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado_soporte")
    private EmpleadoSoporte empleadoSoporte;

    public Integer getId_incidencia() {
        return idIncidencia;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaSolucionTemporal() {
        return fechaSolucionTemporal;
    }

    public void setFechaSolucionTemporal(LocalDateTime fechaSolucionTemporal) {
        this.fechaSolucionTemporal = fechaSolucionTemporal;
    }

    public LocalDateTime getFechaSolucionDefinitiva() {
        return fechaSolucionDefinitiva;
    }

    public void setFechaSolucionDefinitiva(LocalDateTime fechaSolucionDefinitiva) {
        this.fechaSolucionDefinitiva = fechaSolucionDefinitiva;
    }

    public EmpleadoSoporte getEmpleadoSoporte() {
        return empleadoSoporte;
    }

    public void setEmpleadoSoporte(EmpleadoSoporte empleadoSoporte) {
        this.empleadoSoporte = empleadoSoporte;
    }
}
