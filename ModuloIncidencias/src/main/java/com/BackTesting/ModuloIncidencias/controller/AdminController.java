package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.dto.IncidenciaPendienteDTO;
import com.BackTesting.ModuloIncidencias.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
