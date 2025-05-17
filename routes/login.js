const express = require('express');
const router = express.Router();
const pool = require('../db');
const bcrypt = require('bcrypt');

router.get('/', (req, res) => {
  res.render('login/login');
});

router.post('/login', async (req, res) => {
  const { correo, contrasena } = req.body;

  try {
    // Buscar en Cliente
    const clienteResult = await pool.query('SELECT * FROM Cliente WHERE correo = $1', [correo]);
    if (clienteResult.rows.length > 0 && await bcrypt.compare(contrasena, clienteResult.rows[0].contrasena)) {
      req.session.user = clienteResult.rows[0];
      return res.redirect('/cliente/menu');
    }

    // Buscar en EmpleadoSoporte
    const soporteResult = await pool.query('SELECT * FROM EmpleadoSoporte WHERE correo = $1', [correo]);
    if (soporteResult.rows.length > 0 && await bcrypt.compare(contrasena, soporteResult.rows[0].contrasena)) {
      req.session.user = soporteResult.rows[0];
      if (soporteResult.rows[0].rol === 'soporte') {
        return res.redirect('/soporte/menu');
      } else if (soporteResult.rows[0].rol === 'administrador') {
        return res.redirect('/admin/menu');
      }
    }

    res.send('❌ Credenciales inválidas');
  } catch (error) {
    console.error(error);
    res.send('Error al iniciar sesión');
  }
});

module.exports = router;