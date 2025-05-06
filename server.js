const express = require('express');
const app = express();
const path = require('path');
const { Pool } = require('pg');  // Usamos PostgreSQL
const bcrypt = require('bcrypt'); // Añadir bcrypt

// Configurar middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static('public'));

// Configurar la conexión a la base de datos de PostgreSQL
const pool = require('./db/connection');

// Ruta de login
app.post('/login', (req, res) => {
    const { correo, contrasena } = req.body;  // Solo recibimos correo y contraseña
    
    // Primero, buscamos en la tabla Cliente
    let query = 'SELECT * FROM Cliente WHERE correo = $1';
    let tipo_usuario = 'cliente';

    pool.query(query, [correo], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Error en la base de datos' });
        }

        if (result.rows.length === 0) {
            // Si no se encuentra el cliente, buscamos en la tabla de soporte
            query = 'SELECT * FROM EmpleadoSoporte WHERE correo = $1';
            tipo_usuario = 'soporte';  // Asumimos que si no es cliente, es soporte
            
            pool.query(query, [correo], (err, result) => {
                if (err) {
                    return res.status(500).json({ error: 'Error en la base de datos' });
                }

                if (result.rows.length === 0) {
                    return res.status(400).json({ error: 'Usuario no encontrado' });
                }

                // Si lo encontramos en la tabla EmpleadoSoporte, comparamos la contraseña
                const usuario = result.rows[0];
                bcrypt.compare(contrasena, usuario.contrasena, (err, isMatch) => {
                    if (err) {
                        return res.status(500).json({ error: 'Error en la comparación de contraseñas' });
                    }

                    if (!isMatch) {
                        return res.status(400).json({ error: 'Contraseña incorrecta' });
                    }

                    // Redirige al soporte
                    res.redirect('/soporte');
                });
            });
        } else {
            // Si lo encontramos en la tabla Cliente, comparamos la contraseña
            const usuario = result.rows[0];
            bcrypt.compare(contrasena, usuario.contrasena, (err, isMatch) => {
                if (err) {
                    return res.status(500).json({ error: 'Error en la comparación de contraseñas' });
                }

                if (!isMatch) {
                    return res.status(400).json({ error: 'Contraseña incorrecta' });
                }

                // Redirige al cliente (centro de ayuda)
                res.redirect('/cliente');
            });
        }
    });
});

// Página de login
app.get('/', (req, res) => res.sendFile(path.join(__dirname, 'public/login.html')));

// Página de cliente (centro de ayuda)
app.get('/cliente', (req, res) => res.sendFile(path.join(__dirname, 'public/cliente.html')));

// Página de soporte
app.get('/soporte', (req, res) => res.sendFile(path.join(__dirname, 'public/soporte.html')));

// Ruta para obtener los reportes del usuario
app.get('/get-reportes', (req, res) => {
    const usuarioId = req.user.id; // Suponiendo que el ID del usuario está guardado en la sesión o JWT
    
    const query = 'SELECT * FROM Reporte WHERE id_cliente = $1';
    
    pool.query(query, [usuarioId], (err, result) => {
        if (err) {
            console.log(err); // Ver detalles del error en la consola
            return res.status(500).json({ error: 'Error al obtener los reportes' });
        }
        
        console.log(result.rows); // Verificar los resultados antes de devolverlos
        res.json(result.rows); // Devolver los reportes en formato JSON
    });
});

// Ruta para registrar un nuevo reporte
app.post('/reportar-reporte', (req, res) => {
    const { titulo, categoria, descripcion } = req.body;
    const usuarioId = req.user.id; // Suponiendo que el ID del usuario está guardado en la sesión o JWT
    
    const query = `
        INSERT INTO Reporte (id_cliente, titulo, tipo_reporte, descripcion, fecha_creacion)
        VALUES ($1, $2, $3, $4, CURRENT_TIMESTAMP) RETURNING id_reporte;
    `;
    
    pool.query(query, [usuarioId, titulo, categoria, descripcion], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Error al registrar el reporte' });
        }
        
        const idReporte = result.rows[0].id_reporte;
        
        // Crear una incidencia relacionada con el reporte
        const incidenciaQuery = `
            INSERT INTO Incidencia (id_reporte, estado)
            VALUES ($1, 'Pendiente');
        `;
        
        pool.query(incidenciaQuery, [idReporte], (err) => {
            if (err) {
                return res.status(500).json({ error: 'Error al crear la incidencia' });
            }
            res.json({ success: 'Reporte registrado con éxito' });
        });
    });
});
// Iniciar servidor
app.listen(3000, () => {
    console.log('Servidor en el puerto 3000');
});
