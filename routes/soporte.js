const express = require('express');
const router = express.Router();
const pool = require('../db');

router.get('/menu', (req, res) => {
  res.render('soporte/menu');
});

router.get('/incidenciasPendientes', async (req, res) => {
  const id_empleado = req.session.user.id_empleado;
  try {
    const result = await pool.query(`
      SELECT * FROM Incidencia
      WHERE id_empleado_soporte = $1 AND estado != 'Resuelto'
    `, [id_empleado]);
    const incidencias = result.rows;
    res.render('soporte/incidenciasPendientes', { incidencias });
  } catch (error) {
    console.error(error);
    res.send('Error al cargar incidencias pendientes');
  }
});

router.get('/solucionesTerminadas', async (req, res) => {
  const id_empleado = req.session.user.id_empleado;
  try {
    const result = await pool.query(`
      SELECT * FROM Incidencia
      WHERE id_empleado_soporte = $1 AND estado = 'Resuelto'
    `, [id_empleado]);
    const incidencias = result.rows;
    res.render('soporte/solucionesTerminadas', { incidencias });
  } catch (error) {
    console.error(error);
    res.send('Error al cargar soluciones terminadas');
  }
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
      WHERE id_empleado_soporte = $1
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

// Nueva ruta GET para mostrar formulario de solución
router.get('/solucion/:id_incidencia', async (req, res) => {
  const { id_incidencia } = req.params;
  try {
    const result = await pool.query('SELECT * FROM Incidencia WHERE id_incidencia = $1', [id_incidencia]);
    if (result.rows.length === 0) {
      return res.status(404).send('Incidencia no encontrada');
    }
    const incidencia = result.rows[0];
    res.render('soporte/solucion', { incidencia });
  } catch (error) {
    console.error(error);
    res.send('Error al cargar formulario de solución');
  }
});

// Nueva ruta POST para guardar solución y actualizar estado de incidencia
router.post('/solucion', async (req, res) => {
  const { id_incidencia, tipo_solucion, descripcion } = req.body;
  const id_empleado_solucion = req.session.user.id_empleado;

  try {
    // Insertar solución
    await pool.query(`
      INSERT INTO Soluciones (id_incidencia, tipo_solucion, descripcion, id_empleado_solucion)
      VALUES ($1, $2, $3, $4)
    `, [id_incidencia, tipo_solucion, descripcion, id_empleado_solucion]);

    // Actualizar estado y comentario de incidencia según tipo de solución
    let nuevoEstado = null;
    if (tipo_solucion === 'temporal') {
      nuevoEstado = 'En Progreso';
    } else if (tipo_solucion === 'definitiva') {
      nuevoEstado = 'Resuelto';
    }

    if (nuevoEstado) {
      await pool.query(`
        UPDATE Incidencia
        SET estado = $1, comentario = $2, fecha_actualizacion = CURRENT_TIMESTAMP
        WHERE id_incidencia = $3
      `, [nuevoEstado, descripcion, id_incidencia]);
    }

    res.redirect('/soporte/menu');
  } catch (error) {
    console.error(error);
    res.send('Error al guardar solución');
  }
});

module.exports = router;
