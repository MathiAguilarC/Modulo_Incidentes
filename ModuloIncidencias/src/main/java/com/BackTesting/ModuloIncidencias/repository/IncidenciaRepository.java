package com.BackTesting.ModuloIncidencias.repository;

import com.BackTesting.ModuloIncidencias.model.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {}