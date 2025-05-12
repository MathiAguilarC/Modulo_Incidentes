# Módulo de Incidencias - Sistema de Reportes para Clientes y Soporte

Este proyecto es un sistema de gestión de incidencias diseñado para una tienda en línea. Permite a los clientes generar reportes de incidencias, mientras que los empleados de soporte pueden gestionar y actualizar los reportes según su estado.

## Funcionalidades

### 1. **Login y Gestión de Usuarios**
   - **Clientes** y **empleados de soporte** pueden iniciar sesión usando sus credenciales (correo electrónico y contraseña).
   - Las contraseñas de los usuarios se almacenan de forma segura utilizando **bcrypt** para la encriptación.

### 2. **Menú para Clientes**
   - **Generar Reporte**: Los clientes pueden crear un nuevo reporte con un título, descripción y tipo de error (pago, devolución, producto, u otro).
   - **Ver Reportes Enviados**: Los clientes pueden ver los reportes que han enviado previamente, con detalles sobre cada uno.
   - **Cerrar Sesión**: Los clientes pueden cerrar sesión y volver a la pantalla de login.

### 3. **Menú para Soporte**
   - **Ver Reportes**: Los empleados de soporte pueden ver una lista de todos los reportes generados por los clientes.
   - **Asignar Reportes**: Los empleados de soporte pueden asignar reportes a sí mismos para gestionar la resolución.
   - **Actualizar Reportes**: Los empleados de soporte pueden actualizar el estado de las incidencias, cambiándolas a "Pendiente", "En Progreso", "Resuelto" o "Cerrado".
   - **Cerrar Sesión**: Los empleados de soporte pueden cerrar sesión y volver a la pantalla de login.

### 4. **Base de Datos**
   El sistema usa una base de datos PostgreSQL con las siguientes tablas:
   - **Cliente**: Almacena la información de los clientes, incluyendo correo electrónico y contraseñas cifradas.
   - **EmpleadoSoporte**: Almacena la información de los empleados de soporte.
   - **Reporte**: Almacena los reportes generados por los clientes.
   - **Incidencia**: Almacena las incidencias relacionadas con los reportes, incluyendo su estado y prioridad.
   - **Soluciones**: Almacena las soluciones temporales y definitivas aplicadas a las incidencias.

### 5. **Funciones de Seguridad**
   - **Autenticación de Usuarios**: Utiliza el correo y la contraseña para verificar la identidad de los usuarios.
   - **Encriptación de Contraseñas**: Las contraseñas de los usuarios se encriptan utilizando **bcrypt** para asegurar que la información sensible no sea almacenada en texto claro.

## Requisitos

- Python 3.8 o superior
- PostgreSQL 12 o superior
- Las siguientes librerías de Python:
  - `psycopg2`: Para interactuar con la base de datos PostgreSQL.
  - `bcrypt`: Para encriptar y verificar contraseñas.
  - `tkinter`: Para la interfaz gráfica de usuario (GUI).

Puedes instalar las librerías necesarias con el siguiente comando:

```bash
pip install psycopg2 bcrypt
```
## Cómo ejecutar el proyecto
### 1. Configura la base de datos:
Asegúrate de tener PostgreSQL instalado y configurado.
Crea la base de datos tiendasoporte en PostgreSQL y asegúrate de que las tablas estén correctamente definidas con los esquemas proporcionados.

### 2. Ejecuta el programa:
Abre una terminal en la carpeta del proyecto.

Ejecuta el archivo insertardatos.py:
```bash
python insertardatos.py
```
Esta te dara los 2 primeros usuarios para que puedas probar la aplicacion
#### El cliente
Usuario : juan.perez@cliente.com
Contraseña : cliente123
#### El usuario
Usuario : maria.lopez@soporte.com
Contraseña : soporte123

Luego Ejecuta el archivo login_integrado.py:
```bash
python login_integrado.py
```
### 3. Interfaz de Usuario:
Se abrirá una ventana de login. Ingresa las credenciales de un cliente o empleado de soporte para acceder al sistema.
Si eres cliente, podrás generar reportes y ver los que ya has enviado.
Si eres soporte, podrás gestionar los reportes asignados y actualizar su estado.

Estructura del Proyecto
```bash
/proyecto
  ├── login_integrado.py      # Código principal del programa
  ├── insertardatos.py        # Script para insertar datos de prueba en la base de datos
  └── README.md               # Este archivo
```

## Contribuciones

Haz un fork de este repositorio.
Crea una rama para tu cambio (git checkout -b feature/nueva-funcionalidad).
Haz los cambios y haz commit (git commit -am 'Agregada nueva funcionalidad').
Haz push a la rama (git push origin feature/nueva-funcionalidad).
Abre un Pull Request.