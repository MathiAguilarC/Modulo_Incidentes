-- Actualizacion de BD a Postgress
-- Tabla Cliente
CREATE TABLE Cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(15) NOT NULL
);

-- Tabla EmpleadoSoporte
CREATE TABLE EmpleadoSoporte (
    id_empleado SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'soporte' CHECK (rol IN ('soporte', 'administrador'))
);

-- Tabla Reporte
CREATE TABLE Reporte (
    id_reporte SERIAL PRIMARY KEY,
    id_cliente INT,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    tipo_error VARCHAR(50) CHECK (tipo_error IN ('pago', 'devolucion', 'producto', 'otro')) NOT NULL,
    fecha_reporte TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- Tabla Incidencia
CREATE TABLE Incidencia (
    id_incidencia SERIAL PRIMARY KEY,
    id_reporte INT,
    estado VARCHAR(20) CHECK (estado IN ('Pendiente', 'En Progreso', 'Resuelto', 'Cerrado')) NOT NULL,
    prioridad VARCHAR(10) CHECK (prioridad IN ('Alta', 'Media', 'Baja')) NOT NULL,
    fecha_reporte TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_empleado_soporte INT,
    comentario VARCHAR(255),
    fecha_solucion_temporal TIMESTAMP,
    fecha_solucion_definitiva TIMESTAMP,
    FOREIGN KEY (id_reporte) REFERENCES Reporte(id_reporte),
    FOREIGN KEY (id_empleado_soporte) REFERENCES EmpleadoSoporte(id_empleado)
);

-- Tabla Soluciones (para manejar soluciones temporales y definitivas)
CREATE TABLE Soluciones (
    id_solucion SERIAL PRIMARY KEY,
    id_incidencia INT,  -- Relación con Incidencia
    tipo_solucion VARCHAR(20) CHECK (tipo_solucion IN ('temporal', 'definitiva')) NOT NULL,
    descripcion TEXT NOT NULL,
    id_empleado_solucion INT,  -- Empleado que gestionó la solución
    fecha_aplicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (id_empleado_solucion) REFERENCES EmpleadoSoporte(id_empleado)  -- Relaciona con la tabla EmpleadoSoporte
);  