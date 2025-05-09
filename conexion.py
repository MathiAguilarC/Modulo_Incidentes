import psycopg2
from tkinter import messagebox

db_params = {
    'user': 'postgres',
    'host': 'localhost',
    'database': 'tiendasoporte',
    'password': 'root',
    'port': 5432
}

def conectar():
    try:
        conn = psycopg2.connect(**db_params)
        return conn
    except Exception as e:
        messagebox.showerror("Error de Conexión", f"No se pudo conectar a la base de datos: {e}")
        return None

#BASE DE DATOS EN SQL :

#CREATE DATABASE IF NOT EXISTS tiendaSoporte;
#USE tiendaSoporte;

#CREATE TABLE IF NOT EXISTS incidencia (
#    id INT AUTO_INCREMENT PRIMARY KEY,
#    titulo VARCHAR(100),
#    descripcion TEXT,
#    prioridad VARCHAR(20),
#    categoria VARCHAR(50),
#    fecha DATE,
#    estado VARCHAR(30),
#    responsable VARCHAR(100),
#    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
#    id_usuario INT
#);
