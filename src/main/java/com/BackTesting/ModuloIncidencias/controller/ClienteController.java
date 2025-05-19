package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.dto.ReporteConEstadoDTO;
import com.BackTesting.ModuloIncidencias.model.Cliente;
import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.ClienteRepository;
import com.BackTesting.ModuloIncidencias.repository.IncidenciaRepository;
import com.BackTesting.ModuloIncidencias.repository.ReporteRepository;
import com.BackTesting.ModuloIncidencias.service.ReporteService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ReporteService reporteService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReporteRepository reporteRepo;
    @Autowired
    private IncidenciaRepository incidenciaRepo;


    @PostMapping("/generar")
    public ResponseEntity<String> generarReporte(@RequestBody Reporte reporte) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName(); // Este es el correo extraído del token

        // Busca al cliente en base al correo
        Cliente cliente = clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        reporte.setCliente(cliente); // ¡Aquí se vincula el cliente!
        reporteService.crearReporteYIncidencia(reporte);

        return ResponseEntity.ok("Reporte creado correctamente");
    }
    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteConEstadoDTO>> verReportesDelCliente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();

        Cliente cliente = clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Reporte> reportes = reporteRepo.findByCliente(cliente);

        List<ReporteConEstadoDTO> resultado = reportes.stream().map(reporte -> {
            Incidencia incidencia = incidenciaRepo.findByReporte(reporte)
                    .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
            return new ReporteConEstadoDTO(
                    reporte.getTitulo(),
                    reporte.getDescripcion(),
                    reporte.getTipo_error(),
                    reporte.getFechaReporte(),
                    incidencia.getEstado()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

}
