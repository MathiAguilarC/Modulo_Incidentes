package com.BackTesting.ModuloIncidencias.repository;

import com.BackTesting.ModuloIncidencias.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByCorreo(String correo);
}