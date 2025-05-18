package com.BackTesting.ModuloIncidencias.repository;

import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    Optional<Incidencia> findByReporte(Reporte reporte);

}