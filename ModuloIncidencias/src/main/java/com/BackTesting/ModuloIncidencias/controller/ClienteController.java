package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.model.Reporte;
import com.BackTesting.ModuloIncidencias.service.ReporteService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/generar")
    public ResponseEntity<String> generarReporte(@RequestBody Reporte reporte,
                                                 @AuthenticationPrincipal User user) {

        // Aquí deberías buscar el Cliente por el correo del user para asignar id_cliente
        // Esto depende de cómo manejes la seguridad y sesión, pero por ejemplo:
        // Cliente cliente = clienteRepository.findByCorreo(user.getUsername());
        // reporte.setCliente(cliente);

        reporteService.crearReporteYIncidencia(reporte);

        return ResponseEntity.ok("Reporte e incidencia creados con éxito");
    }
}
