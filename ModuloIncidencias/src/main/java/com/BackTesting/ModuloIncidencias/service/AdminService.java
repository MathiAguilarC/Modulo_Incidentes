package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.dto.IncidenciaPendienteDTO;
import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private IncidenciaRepository incidenciaRepo;

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
}
