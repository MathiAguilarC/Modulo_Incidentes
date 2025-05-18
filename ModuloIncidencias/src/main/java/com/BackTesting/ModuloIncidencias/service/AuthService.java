package com.BackTesting.ModuloIncidencias.service;

import com.BackTesting.ModuloIncidencias.dto.LoginResponse;
import com.BackTesting.ModuloIncidencias.model.Cliente;
import com.BackTesting.ModuloIncidencias.model.EmpleadoSoporte;
import com.BackTesting.ModuloIncidencias.repository.ClienteRepository;
import com.BackTesting.ModuloIncidencias.repository.EmpleadoSoporteRepository;
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

    public LoginResponse login(String correo, String contrasena) {
        Optional<Cliente> clienteOpt = clienteRepo.findByCorreo(correo);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (passwordEncoder.matches(contrasena, cliente.getContrasena())) {
                return new LoginResponse("cliente", null, "Login exitoso");
            }
            return new LoginResponse(null, null, "Contraseña incorrecta");
        }

        Optional<EmpleadoSoporte> empOpt = empleadoRepo.findByCorreo(correo);
        if (empOpt.isPresent()) {
            EmpleadoSoporte emp = empOpt.get();
            if (passwordEncoder.matches(contrasena, emp.getContrasena())) {
                return new LoginResponse("soporte", emp.getRol(), "Login exitoso");
            }
            return new LoginResponse(null, null, "Contraseña incorrecta");
        }

        return new LoginResponse(null, null, "Usuario no encontrado");
    }
}
