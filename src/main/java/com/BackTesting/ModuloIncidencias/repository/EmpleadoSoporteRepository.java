package com.BackTesting.ModuloIncidencias.repository;

import com.BackTesting.ModuloIncidencias.model.EmpleadoSoporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoSoporteRepository extends JpaRepository<EmpleadoSoporte, Integer> {
    Optional<EmpleadoSoporte> findByCorreo(String correo);
}