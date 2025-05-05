const express = require('express');
const router = express.Router();
const pool = require('../db/connection');

// Buscar cliente por nombre
router.get('/buscar', async (req, res) => {
  const { nombre } = req.query;
  try {
    const result = await pool.query('SELECT * FROM Cliente WHERE nombre = $1', [nombre]);
    if (result.rows.length > 0) {
      res.json(result.rows[0]);
    } else {
      res.json(null);
    }
  } catch (err) {
    res.status(500).json({ error: 'Error al buscar cliente' });
  }
});

// Crear cliente si no existe y luego crear reporte
router.post('/crear-reporte', async (req, res) => {
  const { nombre, correo, telefono, titulo, descripcion, tipo_error } = req.body;

  try {
    let clienteId;

    const existing = await pool.query('SELECT id_cliente FROM Cliente WHERE nombre = $1', [nombre]);
    if (existing.rows.length > 0) {
      clienteId = existing.rows[0].id_cliente;
    } else {
      const insertCliente = await pool.query(
        'INSERT INTO Cliente (nombre, correo, telefono) VALUES ($1, $2, $3) RETURNING id_cliente',
        [nombre, correo, telefono]
      );
      clienteId = insertCliente.rows[0].id_cliente;
    }

    await pool.query(
      'INSERT INTO Reporte (id_cliente, titulo, descripcion, tipo_error) VALUES ($1, $2, $3, $4)',
      [clienteId, titulo, descripcion, tipo_error]
    );

    res.sendStatus(200);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al crear cliente o reporte' });
  }
});

module.exports = router;