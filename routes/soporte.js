const express = require('express');
const router = express.Router();
const pool = require('../db/connection');

router.get('/incidencias', async (req, res) => {
  try {
    const result = await pool.query(`
      SELECT i.id_incidencia, r.descripcion AS titulo, i.estado, i.prioridad, e.nombre AS responsable
      FROM Incidencia i
      JOIN Reporte r ON i.id_reporte = r.id_reporte
      LEFT JOIN EmpleadoSoporte e ON i.id_empleado_soporte = e.id_empleado
      ORDER BY i.id_incidencia DESC
    `);
    res.json(result.rows);
  } catch (error) {
    console.error(error);
    res.status(500).json({ mensaje: 'Error al obtener incidencias' });
  }
});

module.exports = router;