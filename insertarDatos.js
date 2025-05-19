const { Pool } = require('pg');
const bcrypt = require('bcrypt');

// Configuración de conexión
const dbParams = {
  user: 'postgres',
  host: 'localhost',
  database: 'tiendasoporte',
  password: 'root',
  port: 5432,
};

const pool = new Pool(dbParams);

async function encriptarContrasena(contrasena) {
  const saltRounds = 10;
  const hash = await bcrypt.hash(contrasena, saltRounds);
  return hash;
}

async function insertarCliente() {
  const nombre = 'Juan Pérez';
  const correo = 'juan.perez@cliente.com';
  const telefono = '987654321';
  const contrasena = 'cliente123';

  try {
    const hash = await encriptarContrasena(contrasena);
    const query = `
      INSERT INTO Cliente (nombre, correo, telefono, contrasena)
      VALUES ($1, $2, $3, $4)
    `;
    await pool.query(query, [nombre, correo, telefono, hash]);
    console.log('✅ Cliente insertado exitosamente.');
  } catch (error) {
    console.error('❌ Error insertando cliente:', error.message);
  }
}

async function insertarEmpleadoSoporte() {
  const nombre = 'María López';
  const correo = 'maria.lopez@soporte.com';
  const telefono = '912345678';
  const contrasena = 'soporte123';

  try {
    const hash = await encriptarContrasena(contrasena);
    const query = `
      INSERT INTO EmpleadoSoporte (nombre, correo, telefono, contrasena)
      VALUES ($1, $2, $3, $4)
    `;
    await pool.query(query, [nombre, correo, telefono, hash]);
    console.log('✅ Empleado de soporte insertado exitosamente.');
  } catch (error) {
    console.error('❌ Error insertando soporte:', error.message);
  }
}

async function probarConexion() {
  try {
    await pool.connect();
    console.log('✅ Conexión a la base de datos exitosa.');
  } catch (err) {
    console.error('❌ Error de conexión a la base de datos:', err.message);
  }
}

(async () => {
  await probarConexion();
  await insertarCliente();
  await insertarEmpleadoSoporte();
  await pool.end();
})();