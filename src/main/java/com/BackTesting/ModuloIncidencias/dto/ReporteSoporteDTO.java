package com.BackTesting.ModuloIncidencias.dto;

import java.time.LocalDateTime;

public class ReporteSoporteDTO {
    private Integer idReporte;
    private String titulo;
    private String descripcion;
    private String tipoError;
    private LocalDateTime fechaReporte;
    private String estadoIncidencia;
    private String prioridad;

    public ReporteSoporteDTO() {
    }

    public ReporteSoporteDTO(Integer idReporte, String titulo, String descripcion, String tipoError,
                             LocalDateTime fechaReporte, String estadoIncidencia, String prioridad) {
        this.idReporte = idReporte;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoError = tipoError;
        this.fechaReporte = fechaReporte;
        this.estadoIncidencia = estadoIncidencia;
        this.prioridad = prioridad;
    }

    public Integer getIdReporte() {
        return idReporte;
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

    public String getTipoError() {
        return tipoError;
    }

    public void setTipoError(String tipoError) {
        this.tipoError = tipoError;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getEstadoIncidencia() {
        return estadoIncidencia;
    }

    public void setEstadoIncidencia(String estadoIncidencia) {
        this.estadoIncidencia = estadoIncidencia;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
