package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.dto.ReporteSoporteDTO;
import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoporteService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

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
}