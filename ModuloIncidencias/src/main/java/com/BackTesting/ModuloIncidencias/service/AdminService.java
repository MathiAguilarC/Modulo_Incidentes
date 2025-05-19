package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.dto.AsignarIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.DetalleIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.IncidenciaPendienteDTO;
import com.BackTesting.ModuloIncidencias.dto.SolucionDTO;
import com.BackTesting.ModuloIncidencias.model.EmpleadoSoporte;
import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.EmpleadoSoporteRepository;
import com.BackTesting.ModuloIncidencias.repository.IncidenciaRepository;
import com.BackTesting.ModuloIncidencias.repository.SolucionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private IncidenciaRepository incidenciaRepo;
    @Autowired
    private EmpleadoSoporteRepository empleadoRepo;
    @Autowired
    private SolucionesRepository solucionesRepo;


    public List<IncidenciaPendienteDTO> obtenerIncidenciasPendientes() {
        List<Incidencia> incidencias = incidenciaRepo.findIncidenciasPendientesOSinAsignar();

        return incidencias.stream().map(i -> {
            Reporte r = i.getReporte();
            return new IncidenciaPendienteDTO(
                    i.getId_incidencia(),
                    r.getTitulo(),
                    r.getDescripcion(),
                    r.getTipo_error(),
                    r.getFechaReporte(),
                    i.getEstado(),
                    i.getPrioridad()
            );
        }).collect(Collectors.toList());
    }

    public void asignarIncidencia(AsignarIncidenciaDTO dto) {
        // Buscar incidencia
        Incidencia incidencia = incidenciaRepo.findById(dto.getIdIncidencia())
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

        // Buscar empleado soporte
        EmpleadoSoporte soporte = empleadoRepo.findById(dto.getIdEmpleadoSoporte())
                .orElseThrow(() -> new RuntimeException("Empleado soporte no encontrado"));

        // Validar rol
        if (!"soporte".equalsIgnoreCase(soporte.getRol())) {
            throw new RuntimeException("El empleado no tiene rol de soporte");
        }

        // Asignar soporte y actualizar estado
        incidencia.setEmpleadoSoporte(soporte);
        incidencia.setEstado("En Progreso");
        incidencia.setFechaActualizacion(LocalDateTime.now());

        incidenciaRepo.save(incidencia);
    }

    public DetalleIncidenciaDTO obtenerDetallesAdministrativo(Integer idIncidencia) {
        Incidencia incidencia = incidenciaRepo.findById(idIncidencia)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));

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
