const express = require('express');
const router = express.Router();
const pool = require('../db');

router.get('/menu', (req, res) => {
  res.render('cliente/menu');
});

router.get('/generar', (req, res) => {
  res.render('cliente/generarReporte');
});

router.post('/generar', async (req, res) => {
  const { titulo, descripcion, tipo_error } = req.body;
  const id_cliente = req.session.user.id_cliente;

  try {
    await pool.query(`
      INSERT INTO Reporte (id_cliente, titulo, descripcion, tipo_error)
      VALUES ($1, $2, $3, $4)
    `, [id_cliente, titulo, descripcion, tipo_error]);

    res.redirect('/cliente/menu');
  } catch (error) {
    console.error(error);
    res.send('Error al generar el reporte');
  }
});

router.get('/reportes', async (req, res) => {
  const id_cliente = req.session.user.id_cliente;

  try {
    const result = await pool.query(`
      SELECT * FROM Reporte WHERE id_cliente = $1
    `, [id_cliente]);

    res.render('cliente/verReportes', { reportes: result.rows });
  } catch (error) {
    console.error(error);
    res.send('Error al obtener reportes');
  }
});

module.exports = router;
