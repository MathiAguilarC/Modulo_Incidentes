package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.dto.ActualizarIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.DetalleIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.ReporteSoporteDTO;
import com.BackTesting.ModuloIncidencias.model.EmpleadoSoporte;
import com.BackTesting.ModuloIncidencias.repository.EmpleadoSoporteRepository;
import com.BackTesting.ModuloIncidencias.service.SoporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/soporte")
public class SoporteController {

    @Autowired
    private SoporteService soporteService;

    @Autowired
    private EmpleadoSoporteRepository empleadoSoporteRepository;

    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteSoporteDTO>> verReportesDelSoporte() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();

        EmpleadoSoporte empleado = empleadoSoporteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Empleado soporte no encontrado"));

        List<ReporteSoporteDTO> reportes = soporteService.obtenerReportesPorEmpleado(empleado.getId_empleado());

        return ResponseEntity.ok(reportes);
    }
    @PutMapping("/incidencia")
    public ResponseEntity<String> actualizarIncidencia(@RequestBody ActualizarIncidenciaDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();

        EmpleadoSoporte empleado = empleadoSoporteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        soporteService.actualizarIncidencia(empleado.getId_empleado(), dto);

        return ResponseEntity.ok("Incidencia actualizada correctamente");
    }
    @GetMapping("/incidencia/{id}")
    public ResponseEntity<DetalleIncidenciaDTO> getDetallesIncidencia(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();

        EmpleadoSoporte soporte = empleadoSoporteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        DetalleIncidenciaDTO detalles = soporteService.obtenerDetallesDeIncidencia(soporte.getId_empleado(), id);

        return ResponseEntity.ok(detalles);
    }
}
