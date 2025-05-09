import mysql.connector

def conectar():
    return mysql.connector.connect(
        host="localhost",
        user="root",         
        password="",         
        database="tiendaSoporte"
    )


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