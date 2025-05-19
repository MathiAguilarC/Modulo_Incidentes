package com.BackTesting.ModuloIncidencias.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DetalleIncidenciaDTO {
    private Integer idIncidencia;
    private String titulo;
    private String descripcion;
    private String tipoError;
    private LocalDateTime fechaReporte;

    private String estado;
    private String prioridad;
    private String comentario;
    private LocalDateTime fechaActualizacion;

    private List<SolucionDTO> historialSoluciones;

    // constructor

    public DetalleIncidenciaDTO(Integer idIncidencia, String titulo, String descripcion, String tipoError, LocalDateTime fechaReporte, String estado, String prioridad, String comentario, LocalDateTime fechaActualizacion, List<SolucionDTO> historialSoluciones) {
        this.idIncidencia = idIncidencia;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoError = tipoError;
        this.fechaReporte = fechaReporte;
        this.estado = estado;
        this.prioridad = prioridad;
        this.comentario = comentario;
        this.fechaActualizacion = fechaActualizacion;
        this.historialSoluciones = historialSoluciones;
    }


    // Getters, setters


    public Integer getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Integer idIncidencia) {
        this.idIncidencia = idIncidencia;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<SolucionDTO> getHistorialSoluciones() {
        return historialSoluciones;
    }

    public void setHistorialSoluciones(List<SolucionDTO> historialSoluciones) {
        this.historialSoluciones = historialSoluciones;
    }
}