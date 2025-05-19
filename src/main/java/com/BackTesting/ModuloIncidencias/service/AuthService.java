package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.dto.LoginResponse;
import com.BackTesting.ModuloIncidencias.model.Cliente;
import com.BackTesting.ModuloIncidencias.model.EmpleadoSoporte;
import com.BackTesting.ModuloIncidencias.repository.ClienteRepository;
import com.BackTesting.ModuloIncidencias.repository.EmpleadoSoporteRepository;
import com.BackTesting.ModuloIncidencias.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private EmpleadoSoporteRepository empleadoRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String correo, String contrasena) {
        Optional<Cliente> clienteOpt = clienteRepo.findByCorreo(correo);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (passwordEncoder.matches(contrasena, cliente.getContrasena())) {
                return jwtUtil.generateToken(cliente.getCorreo(), "cliente", null);
            }
        }

        Optional<EmpleadoSoporte> empOpt = empleadoRepo.findByCorreo(correo);
        if (empOpt.isPresent()) {
            EmpleadoSoporte emp = empOpt.get();
            if (passwordEncoder.matches(contrasena, emp.getContrasena())) {
                return jwtUtil.generateToken(emp.getCorreo(), "soporte", emp.getRol());
            }
        }

        throw new RuntimeException("Credenciales inválidas");
    }
}
