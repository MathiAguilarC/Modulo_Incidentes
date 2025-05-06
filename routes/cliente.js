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

// Crear reporte sin verificar cliente (ya está autenticado)
router.post('/crear-reporte', async (req, res) => {
  const { titulo, descripcion, categoria } = req.body;

  try {
    // Obtén el id_cliente directamente desde la sesión
    const clienteId = req.session.userId; // Asegúrate de que 'userId' se haya guardado en la sesión al hacer login

    if (!clienteId) {
      return res.status(401).json({ error: 'No estás autenticado' });
    }

    // Crear el reporte usando el id_cliente desde la sesión
    await pool.query(
      'INSERT INTO Reporte (id_cliente, titulo, descripcion, tipo_error) VALUES ($1, $2, $3, $4)',
      [clienteId, titulo, descripcion, categoria]
    );

    res.sendStatus(200);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error al crear el reporte' });
  }
});

document.getElementById('form-incidencia').addEventListener('submit', function(event) {
  event.preventDefault();

  const formData = new FormData(this);

  // Enviar los datos del formulario al backend
  fetch('/crear-reporte', {
    method: 'POST',
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    alert('Reporte registrado con éxito');
    document.getElementById('form-incidencia').reset();  // Resetear el formulario
  })
  .catch(error => console.error('Error al reportar incidencia:', error));
});

module.exports = router;