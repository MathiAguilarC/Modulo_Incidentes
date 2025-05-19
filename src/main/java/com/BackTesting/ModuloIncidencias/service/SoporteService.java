package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.dto.ActualizarIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.DetalleIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.ReporteSoporteDTO;
import com.BackTesting.ModuloIncidencias.dto.SolucionDTO;
import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.IncidenciaRepository;
import com.BackTesting.ModuloIncidencias.repository.SolucionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoporteService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;
    @Autowired
    private SolucionesRepository solucionesRepo;

    public List<ReporteSoporteDTO> obtenerReportesPorEmpleado(Integer idEmpleado) {
        List<Incidencia> incidencias = incidenciaRepository.findByEmpleadoSoporteId(idEmpleado);

        return incidencias.stream().map(i -> {
            Reporte r = i.getReporte();
            return new ReporteSoporteDTO(
                    r.getId_reporte(),
                    r.getTitulo(),
                    r.getDescripcion(),
                    r.getTipo_error(),
                    r.getFechaReporte(),
                    i.getEstado(),
                    i.getPrioridad()
            );
        }).collect(Collectors.toList());
    }
    public void actualizarIncidencia(Integer idEmpleado, ActualizarIncidenciaDTO dto) {
        Incidencia incidencia = incidenciaRepository.findById(dto.getIdIncidencia())
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        // Verifica que la incidencia esté asignada al empleado autenticado
        if (incidencia.getEmpleadoSoporte() == null ||
                !incidencia.getEmpleadoSoporte().getId_empleado().equals(idEmpleado)) {
            throw new RuntimeException("No tienes permiso para modificar esta incidencia");
        }

        // Validar estado
        String estado = dto.getNuevoEstado();
        if (!List.of("Pendiente", "En Progreso", "Resuelto", "Cerrado").contains(estado)) {
            throw new RuntimeException("Estado inválido");
        }

        incidencia.setEstado(estado);
        incidencia.setComentario(dto.getComentario());
        incidencia.setFechaActualizacion(LocalDateTime.now());

        incidenciaRepository.save(incidencia);
    }

    public DetalleIncidenciaDTO obtenerDetallesDeIncidencia(Integer idEmpleado, Integer idIncidencia) {
        Incidencia incidencia = incidenciaRepository.findById(idIncidencia)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        if (incidencia.getEmpleadoSoporte() == null ||
                !incidencia.getEmpleadoSoporte().getId_empleado().equals(idEmpleado)) {
            throw new RuntimeException("No tienes permiso para ver esta incidencia");
        }

        Reporte reporte = incidencia.getReporte();

        List<SolucionDTO> historial = solucionesRepo.findByIncidencia_IdIncidencia(idIncidencia).stream()
                .map(sol -> new SolucionDTO(sol.getTipoSolucion(), sol.getDescripcion(), sol.getFechaAplicacion()))
                .collect(Collectors.toList());

        return new DetalleIncidenciaDTO(
                incidencia.getId_incidencia(),
                reporte.getTitulo(),
                reporte.getDescripcion(),
                reporte.getTipo_error(),
                reporte.getFechaReporte(),
                incidencia.getEstado(),
                incidencia.getPrioridad(),
                incidencia.getComentario(),
                incidencia.getFechaActualizacion(),
                historial
        );
    }

}