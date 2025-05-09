import psycopg2
import bcrypt

# Datos de conexión a la base de datos PostgreSQL
db_params = {
    'user': 'postgres',
    'host': 'localhost',
    'database': 'tiendasoporte',
    'password': 'root',
    'port': 5432
}

def conectar_db():
    try:
        conn = psycopg2.connect(**db_params)
        return conn
    except Exception as e:
        print(f"Error de conexión a la base de datos: {e}")
        return None

def encriptar_contraseña(contraseña):
    # Genera una sal aleatoria y encripta la contraseña
    sal = bcrypt.gensalt()
    contrasena_encriptada = bcrypt.hashpw(contraseña.encode('utf-8'), sal)
    # Convertir el hash de bytes a string antes de almacenarlo
    return contrasena_encriptada.decode('utf-8')


def insertar_cliente():
    conn = conectar_db()
    if conn:
        cursor = conn.cursor()
        # Datos de cliente de prueba
        nombre = 'Juan Pérez'
        correo = 'juan.perez@cliente.com'
        telefono = '987654321'
        contraseña = 'cliente123'
        contrasena_encriptada = encriptar_contraseña(contraseña)

        # Insertar cliente en la base de datos
        query_cliente = """
        INSERT INTO Cliente (nombre, correo, telefono, contrasena)
        VALUES (%s, %s, %s, %s);
        """
        cursor.execute(query_cliente, (nombre, correo, telefono, contrasena_encriptada))
        conn.commit()
        print("Cliente insertado exitosamente.")
        conn.close()

def insertar_empleado_soporte():
    conn = conectar_db()
    if conn:
        cursor = conn.cursor()
        # Datos de empleado de soporte de prueba
        nombre = 'María López'
        correo = 'maria.lopez@soporte.com'
        telefono = '912345678'
        contraseña = 'soporte123'
        contrasena_encriptada = encriptar_contraseña(contraseña)

        # Insertar empleado de soporte en la base de datos
        query_soporte = """
        INSERT INTO EmpleadoSoporte (nombre, correo, telefono, contrasena)
        VALUES (%s, %s, %s, %s);
        """
        cursor.execute(query_soporte, (nombre, correo, telefono, contrasena_encriptada))
        conn.commit()
        print("Empleado de soporte insertado exitosamente.")
        conn.close()

# Llamar a las funciones para insertar los datos de prueba
insertar_cliente()
insertar_empleado_soporte()