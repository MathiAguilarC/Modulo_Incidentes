const bcrypt = require('bcrypt');
const { Pool } = require('pg');  // Para PostgreSQL

// Configurar la conexión a PostgreSQL
const pool = new Pool({
    user: 'postgres',       // Cambia con tu usuario de PostgreSQL
    host: 'localhost',
    database: 'tiendasoporte',
    password: 'root',
    port: 5432,
});

// Función para encriptar la contraseña y luego insertar los datos
const insertarDatos = async () => { 
    try {
        // Encriptar la contraseña
        const contrasenaCliente = await bcrypt.hash('cliente123', 10);  // Contraseña para cliente
        const contrasenaSoporte = await bcrypt.hash('soporte123', 10);  // Contraseña para soporte

        // Insertar datos de Cliente
        await pool.query(`
            INSERT INTO Cliente (nombre, correo, telefono, contrasena)
            VALUES
                ('Juan Perez', 'juan.perez@mail.com', '123456789', '${contrasenaCliente}'),
                ('Maria Lopez', 'maria.lopez@mail.com', '987654321', '${contrasenaCliente}');
        `);

        // Insertar datos de Empleado de Soporte
        await pool.query(`
            INSERT INTO EmpleadoSoporte (nombre, correo, telefono, contrasena)
            VALUES
                ('Carlos Gutierrez', 'carlos.gutierrez@soporte.com', '111223344', '${contrasenaSoporte}'),
                ('Laura Fernandez', 'laura.fernandez@soporte.com', '555667788', '${contrasenaSoporte}');
        `);

        console.log('Datos insertados correctamente');
    } catch (err) {
        console.error('Error al insertar los datos:', err);
    } finally {
        pool.end();
    }
};

// Ejecutar la función para insertar los datos
insertarDatos();
