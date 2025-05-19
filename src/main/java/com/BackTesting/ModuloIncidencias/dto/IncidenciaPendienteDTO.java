package com.BackTesting.ModuloIncidencias.dto;

import java.time.LocalDateTime;

public class IncidenciaPendienteDTO {
    private Integer idIncidencia;
    private String tituloReporte;
    private String descripcion;
    private String tipoError;
    private LocalDateTime fechaReporte;
    private String estado;
    private String prioridad;

    // Constructor
    public IncidenciaPendienteDTO(Integer idIncidencia, String tituloReporte, String descripcion,
                                  String tipoError, LocalDateTime fechaReporte,
                                  String estado, String prioridad) {
        this.idIncidencia = idIncidencia;
        this.tituloReporte = tituloReporte;
        this.descripcion = descripcion;
        this.tipoError = tipoError;
        this.fechaReporte = fechaReporte;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public String getTituloReporte() {
        return tituloReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoError() {
        return tipoError;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public String getEstado() {
        return estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    // Getters y setters (opcional si usas lombok)
}
