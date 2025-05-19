package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.dto.AsignarIncidenciaDTO;
import com.BackTesting.ModuloIncidencias.dto.IncidenciaPendienteDTO;
import com.BackTesting.ModuloIncidencias.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/incidencias/pendientes")
    public ResponseEntity<List<IncidenciaPendienteDTO>> verIncidenciasPendientes() {
        List<IncidenciaPendienteDTO> lista = adminService.obtenerIncidenciasPendientes();
        return ResponseEntity.ok(lista);
    }
    @PostMapping("/incidencias/asignar")
    public ResponseEntity<String> asignarIncidencia(@RequestBody AsignarIncidenciaDTO dto) {
        adminService.asignarIncidencia(dto);
        return ResponseEntity.ok("Incidencia asignada correctamente");
    }

}
