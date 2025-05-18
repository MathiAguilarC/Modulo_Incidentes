package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.model.Cliente;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.repository.ClienteRepository;
import com.BackTesting.ModuloIncidencias.service.ReporteService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ReporteService reporteService;
    @Autowired
    private ClienteRepository clienteRepository;

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

}
