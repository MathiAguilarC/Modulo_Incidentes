package com.BackTesting.ModuloIncidencias.dto;

import java.time.LocalDateTime;

public class SolucionDTO {
    private String tipoSolucion; // temporal o definitiva
    private String descripcion;
    private LocalDateTime fechaAplicacion;

    // Constructor,

    public SolucionDTO(String tipoSolucion, String descripcion, LocalDateTime fechaAplicacion) {
        this.tipoSolucion = tipoSolucion;
        this.descripcion = descripcion;
        this.fechaAplicacion = fechaAplicacion;
    }


    // getters y setters

    public String getTipoSolucion() {
        return tipoSolucion;
    }

    public void setTipoSolucion(String tipoSolucion) {
        this.tipoSolucion = tipoSolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDateTime fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
}
