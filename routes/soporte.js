const express = require('express');
const router = express.Router();
const pool = require('../db');

router.get('/menu', (req, res) => {
  res.render('soporte/menu');
});

router.get('/asignados', async (req, res) => {
  const id_empleado = req.session.user.id_empleado;

  try {
    const result = await pool.query(`
      SELECT R.* FROM Incidencia I
      JOIN Reporte R ON I.id_reporte = R.id_reporte
      WHERE I.id_empleado_soporte = $1
    `, [id_empleado]);

    res.render('soporte/reportesAsignados', { reportes: result.rows });
  } catch (error) {
    console.error(error);
    res.send('Error al cargar reportes asignados');
  }
});

router.get('/incidencias', async (req, res) => {
  const id_empleado = req.session.user.id_empleado;

  try {
    const result = await pool.query(`
      SELECT * FROM Incidencia
      WHERE estado = 'Pendiente' AND id_empleado_soporte = $1
    `, [id_empleado]);

    res.render('soporte/incidenciasPendientes', { incidencias: result.rows });
  } catch (error) {
    console.error(error);
    res.send('Error al obtener incidencias');
  }
});

router.get('/actualizar', (req, res) => {
  res.render('soporte/actualizarReporte');
});

router.post('/actualizar', async (req, res) => {
  const { id_incidencia, estado, prioridad } = req.body;

  try {
    await pool.query(`
      UPDATE Incidencia
      SET estado = $1, prioridad = $2, fecha_actualizacion = CURRENT_TIMESTAMP
      WHERE id_incidencia = $3
    `, [estado, prioridad, id_incidencia]);

    res.redirect('/soporte/menu');
  } catch (error) {
    console.error(error);
    res.send('Error al actualizar incidencia');
  }
});

module.exports = router;