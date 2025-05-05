const express = require('express');
const app = express();
const clienteRoutes = require('./routes/cliente');
const soporteRoutes = require('./routes/soporte');
const path = require('path');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static('public'));

app.use('/api/cliente', clienteRoutes);
app.use('/api/soporte', soporteRoutes);

app.get('/', (req, res) => res.sendFile(path.join(__dirname, 'public/cliente.html')));
app.get('/soporte', (req, res) => res.sendFile(path.join(__dirname, 'public/soporte.html')));

app.listen(3000, () => {
  console.log('Servidor corriendo en http://localhost:3000');
});