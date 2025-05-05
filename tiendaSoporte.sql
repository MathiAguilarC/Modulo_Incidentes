-- Tabla Cliente
CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15) NOT NULL
);

-- Tabla EmpleadoSoporte
CREATE TABLE EmpleadoSoporte (
    id_empleado INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15) NOT NULL
);

-- Tabla Reporte
CREATE TABLE Reporte (
    id_reporte INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    descripcion TEXT NOT NULL,
    tipo_error ENUM('pago', 'devolucion', 'producto', 'otro') NOT NULL,
    fecha_reporte DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- Tabla Incidencia
CREATE TABLE Incidencia (
    id_incidencia INT PRIMARY KEY AUTO_INCREMENT,
    id_reporte INT,
    estado ENUM('Pendiente', 'En Progreso', 'Resuelto', 'Cerrado') NOT NULL,
    prioridad ENUM('Alta', 'Media', 'Baja') NOT NULL,
    fecha_reporte DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    id_empleado_soporte INT,
    fecha_solucion_temporal DATETIME,
    fecha_solucion_definitiva DATETIME,
    FOREIGN KEY (id_reporte) REFERENCES Reporte(id_reporte),
    FOREIGN KEY (id_empleado_soporte) REFERENCES EmpleadoSoporte(id_empleado)
);

-- Tabla Soluciones (para manejar soluciones temporales y definitivas)
CREATE TABLE Soluciones (
    id_solucion INT PRIMARY KEY AUTO_INCREMENT,
    id_incidencia INT,  -- Relación con Incidencia
    tipo_solucion ENUM('temporal', 'definitiva') NOT NULL,
    descripcion TEXT NOT NULL,
    id_empleado_solucion INT,  -- Empleado que gestionó la solución (ahora hace referencia al ID del empleado)
    fecha_aplicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_incidencia) REFERENCES Incidencia(id_incidencia),
    FOREIGN KEY (id_empleado_solucion) REFERENCES EmpleadoSoporte(id_empleado)  -- Relaciona con la tabla EmpleadoSoporte
);
