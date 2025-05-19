package com.BackTesting.ModuloIncidencias;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Utilidad para insertar datos de prueba en la base de datos
 * Ejecutar esta clase como una aplicación Java independiente
 */
public class InsertarDatosPrueba {

    // Datos de conexión a la base de datos PostgreSQL
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/tiendasoporte";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "root";
    
    // Instancia de BCryptPasswordEncoder para encriptar contraseñas
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public static void main(String[] args) {
        try (Connection connection = conectarDB()) {
            if (connection != null) {
                insertarCliente(connection);
                insertarEmpleadoSoporte(connection, "soporte");
                insertarEmpleadoSoporte(connection, "administrador");
                System.out.println("Datos insertados exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error durante la inserción de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static Connection conectarDB() {
        Connection connection = null;
        try {
            // Cargar el driver JDBC de PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            // Establecer conexión con la base de datos
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Conexión a la base de datos establecida.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver PostgreSQL no encontrado. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
        return connection;
    }
    
    private static void insertarCliente(Connection connection) throws SQLException {
        // Datos del cliente de prueba
        String nombre = "Juan Pérez";
        String correo = "juan.perez@cliente.com";
        String telefono = "987654321";
        String contrasena = passwordEncoder.encode("cliente123"); // Contraseña encriptada
        
        // Query SQL para insertar cliente
        String queryCliente = "INSERT INTO Cliente (nombre, correo, telefono, contrasena) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryCliente)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, correo);
            preparedStatement.setString(3, telefono);
            preparedStatement.setString(4, contrasena);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente insertado exitosamente.");
            }
        }
    }
    
    private static void insertarEmpleadoSoporte(Connection connection, String rol) throws SQLException {
        // Datos del empleado de soporte, diferentes según el rol
        String nombre, correo, telefono, contrasenaPlana;
        
        if (rol.equals("administrador")) {
            nombre = "Carlos Rodríguez";
            correo = "carlos.rodriguez@admin.com";
            telefono = "555666777";
            contrasenaPlana = "admin123";
        } else { // rol de soporte por defecto
            nombre = "María López";
            correo = "maria.lopez@soporte.com";
            telefono = "912345678";
            contrasenaPlana = "soporte123";
        }
        
        String contrasena = passwordEncoder.encode(contrasenaPlana); // Contraseña encriptada
        
        // Query SQL para insertar empleado de soporte con el rol específico
        String querySoporte = "INSERT INTO EmpleadoSoporte (nombre, correo, telefono, contrasena, rol) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(querySoporte)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, correo);
            preparedStatement.setString(3, telefono);
            preparedStatement.setString(4, contrasena);
            preparedStatement.setString(5, rol);
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Empleado con rol " + rol + " insertado exitosamente.");
            }
        }
    }
}
