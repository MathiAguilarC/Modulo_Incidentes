package com.BackTesting.ModuloIncidencias.repository;

import com.BackTesting.ModuloIncidencias.model.Soluciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolucionesRepository extends JpaRepository<Soluciones, Integer> {
    List<Soluciones> findByIncidencia_IdIncidencia(Integer idIncidencia);
}
