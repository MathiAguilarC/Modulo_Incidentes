package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.IncidenciaRepository;
import com.BackTesting.ModuloIncidencias.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepo;

    @Autowired
    private IncidenciaRepository incidenciaRepo;

    public void crearReporteYIncidencia(Reporte reporte) {
        // Guardar reporte
        reporte.setFechaReporte(LocalDateTime.now());
        Reporte reporteGuardado = reporteRepo.save(reporte);

        // Crear incidencia con prioridad según tipo_error
        Incidencia incidencia = new Incidencia();
        incidencia.setReporte(reporteGuardado);
        incidencia.setEstado("Pendiente");
        incidencia.setFechaReporte(LocalDateTime.now());
        incidencia.setFechaActualizacion(LocalDateTime.now());

        switch (reporte.getTipo_error()) {
            case "pago":
                incidencia.setPrioridad("Alta");
                break;
            case "devolucion":
                incidencia.setPrioridad("Baja");
                break;
            case "producto":
            case "otro":
                incidencia.setPrioridad("Media");
                break;
            default:
                incidencia.setPrioridad("Media");
        }

        incidenciaRepo.save(incidencia);
    }
}
