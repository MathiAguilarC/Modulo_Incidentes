package com.BackTesting.ModuloIncidencias.controller;

import com.BackTesting.ModuloIncidencias.dto.LoginRequest;
import com.BackTesting.ModuloIncidencias.dto.LoginResponse;
import com.BackTesting.ModuloIncidencias.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getCorreo(), request.getContrasena());
        if ("Login exitoso".equals(response.getMensaje())) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
