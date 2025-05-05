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
    const { correo, contrasena, tipo_usuario } = req.body;  // Suponiendo que 'correo', 'contrasena' y 'tipo_usuario' se pasan en el cuerpo del request
    
    let query;
    
    if (tipo_usuario === 'cliente') {
        query = 'SELECT * FROM Cliente WHERE correo = $1';  // Usamos el formato de parámetros de PostgreSQL
    } else if (tipo_usuario === 'soporte') {
        query = 'SELECT * FROM EmpleadoSoporte WHERE correo = $1';
    } else {
        return res.status(400).json({ error: 'Tipo de usuario no válido' });
    }

    pool.query(query, [correo], (err, result) => {
        if (err) {
            return res.status(500).json({ error: 'Error en la base de datos' });
        }

        if (result.rows.length === 0) {
            return res.status(400).json({ error: 'Usuario no encontrado' });
        }

        // Compara la contraseña con el hash almacenado en la base de datos
        const usuario = result.rows[0];
        bcrypt.compare(contrasena, usuario.contrasena, (err, isMatch) => {
            if (err) {
                return res.status(500).json({ error: 'Error en la comparación de contraseñas' });
            }

            if (!isMatch) {
                return res.status(400).json({ error: 'Contraseña incorrecta' });
            }

            // Redirige según el tipo de usuario
            if (tipo_usuario === 'cliente') {
                res.redirect('/cliente');  // Redirige al centro de ayuda (cliente)
            } else if (tipo_usuario === 'soporte') {
                res.redirect('/soporte');  // Redirige al soporte
            }
        });
    });
});

// Página de login
app.get('/', (req, res) => res.sendFile(path.join(__dirname, 'public/login.html')));

// Página de cliente (centro de ayuda)
app.get('/cliente', (req, res) => res.sendFile(path.join(__dirname, 'public/cliente.html')));

// Página de soporte
app.get('/soporte', (req, res) => res.sendFile(path.join(__dirname, 'public/soporte.html')));

// Iniciar servidor
app.listen(3000, () => {
    console.log('Servidor en el puerto 3000');
});