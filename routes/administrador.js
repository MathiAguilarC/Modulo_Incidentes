const express = require('express');
const router = express.Router();
const pool = require('../db');

router.get('/menu', (req, res) => {
  res.render('administrador/menu');
});

router.get('/asignar', async (req, res) => {
  try {
    // Obtener reportes para mostrar en el formulario
    const reportesResult = await pool.query('SELECT * FROM Reporte');
    const reportes = reportesResult.rows;
    // Obtener empleados de soporte para asignar
    const empleadosResult = await pool.query("SELECT id_empleado, nombre, correo, telefono FROM EmpleadoSoporte WHERE rol = 'soporte'");
    const empleados = empleadosResult.rows;
    res.render('administrador/asignarReporte', { reportes, empleados });
  } catch (error) {
    console.error(error);
    res.send('Error al cargar datos para asignar reporte');
  }
});

router.post('/asignar', async (req, res) => {
  const { id_reporte, id_empleado, prioridad } = req.body;

  try {
    await pool.query(`
      INSERT INTO Incidencia (id_reporte, id_empleado_soporte, estado, prioridad)
      VALUES ($1, $2, 'Pendiente', $3)
    `, [id_reporte, id_empleado, prioridad]);

    res.redirect('/admin/menu');
  } catch (error) {
    console.error(error);
    res.send('Error al asignar reporte');
  }
});

module.exports = router;
