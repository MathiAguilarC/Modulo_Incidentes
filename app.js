const express = require('express');
const session = require('express-session');
const bodyParser = require('body-parser');
const loginRoutes = require('./routes/login');
const clienteRoutes = require('./routes/cliente');
const soporteRoutes = require('./routes/soporte');
const administradorRoutes = require('./routes/administrador');
const path = require('path');

const app = express();
const PORT = 3000;

// Configurar motor de vistas
app.set('view engine', 'ejs');

// Middlewares
app.use(express.static('public'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(session({
  secret: 'soporte_secret',
  resave: false,
  saveUninitialized: true
}));
// Rutas principales
app.use('/', loginRoutes);
app.use('/cliente', clienteRoutes);
app.use('/soporte', soporteRoutes);
app.use('/admin', administradorRoutes);
app.use(express.static(path.join(__dirname, 'public')));

// Manejo de rutas inexistentes
app.use((req, res) => {
  res.status(404).send('❌ Página no encontrada');
});

// Iniciar servidor
app.listen(PORT, () => {
  console.log(`✅ Servidor corriendo en http://localhost:${PORT}`);
});