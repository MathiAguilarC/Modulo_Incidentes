Requisitos Previos

- Node.js (versión 20.15.1 o superior recomendada)
- npm (gestor de paquetes de Node.js)
- PostgreSQL (base de datos)
- Java JDK (versión 11 o superior) para el módulo Java
- Maven (para construir el módulo Java)

Instalación y Ejecución

Backend Node.js
- Clonar el repositorio y navegar al directorio del proyecto.
- Instalar las dependencias de Node.js:

npm install
- Configurar la base de datos PostgreSQL y actualizar las credenciales en db.js.
- Ejecutar el servidor Node.js:

node app.js
El servidor estará disponible en http://localhost:3000.

Uso de insertarDatos.js
El archivo insertarDatos.js contiene scripts para insertar datos de prueba en la base de datos (solo testers y desarrolladores).
Importante: Los usuarios y datos insertados por este script son solo para pruebas y demostraciones. Esta información debe ser utilizada únicamente por desarrolladores y testers para validar el funcionamiento del sistema.