const express = require('express');
const router = express.Router();
const pool = require('../db');

router.get('/menu', (req, res) => {
  res.render('administrador/menu');
});

router.get('/asignar', (req, res) => {
  res.render('administrador/asignarReporte');
});

router.post('/asignar', async (req, res) => {
  const { id_reporte, id_empleado } = req.body;

  try {
    await pool.query(`
      INSERT INTO Incidencia (id_reporte, id_empleado_soporte, estado, prioridad)
      VALUES ($1, $2, 'Pendiente', 'Media')
    `, [id_reporte, id_empleado]);

    res.redirect('/admin/menu');
  } catch (error) {
    console.error(error);
    res.send('Error al asignar reporte');
  }
});

module.exports = router;