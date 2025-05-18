package com.BackTesting.ModuloIncidencias.repository;

import com.BackTesting.ModuloIncidencias.model.Incidencia;
import com.BackTesting.ModuloIncidencias.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    Optional<Incidencia> findByReporte(Reporte reporte);
    @Query("SELECT i FROM Incidencia i JOIN FETCH i.reporte r WHERE i.empleadoSoporte.id_empleado = :idEmpleado")
    List<Incidencia> findByEmpleadoSoporteId(@Param("idEmpleado") Integer idEmpleado);

}