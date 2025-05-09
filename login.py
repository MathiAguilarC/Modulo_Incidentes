import tkinter as tk
from tkinter import messagebox
import psycopg2
import bcrypt  # Importar bcrypt para verificar contraseñas

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
        messagebox.showerror("Error de Conexión", f"No se pudo conectar a la base de datos: {e}")
        return None

def mostrar_login():
    # Función para mostrar la pantalla de login
    ventana_login = tk.Tk()
    ventana_login.title("Login")

    # Etiquetas y campos de entrada
    label_correo = tk.Label(ventana_login, text="Correo Electrónico")
    label_correo.pack(pady=5)
    entry_correo = tk.Entry(ventana_login)
    entry_correo.pack(pady=5)

    label_contraseña = tk.Label(ventana_login, text="Contraseña")
    label_contraseña.pack(pady=5)
    entry_contraseña = tk.Entry(ventana_login, show="*")
    entry_contraseña.pack(pady=5)

    # Función de verificación de usuario dentro del contexto de mostrar_login
    def verificar_usuario_interno():
        correo = entry_correo.get()
        contraseña = entry_contraseña.get()

        # Validación de los campos de entrada
        if not correo or not contraseña:
            messagebox.showerror("Error", "Por favor, ingrese todos los campos.")
            return

        conn = conectar_db()
        if conn:
            cursor = conn.cursor()

            # Verificar si el correo pertenece a un Cliente
            query_cliente = "SELECT id_cliente, contrasena FROM Cliente WHERE correo = %s"
            cursor.execute(query_cliente, (correo,))
            cliente = cursor.fetchone()

            # Verificar si el correo pertenece a un EmpleadoSoporte
            query_soporte = "SELECT id_empleado, contrasena FROM EmpleadoSoporte WHERE correo = %s"
            cursor.execute(query_soporte, (correo,))
            soporte = cursor.fetchone()

            if cliente:  # Si es un cliente
                # Verificar si la contraseña coincide con la almacenada en la base de datos
                try:
                    # La contraseña está almacenada como string, necesitamos convertirla a bytes
                    hash_almacenado = cliente[1].encode('utf-8')
                    if bcrypt.checkpw(contraseña.encode('utf-8'), hash_almacenado):
                        ventana_login.destroy()  # Cerrar ventana de login
                        mostrar_menu_cliente(correo)  # Pasar el correo como referencia
                    else:
                        messagebox.showerror("Error", "Correo o contraseña incorrectos.")
                except ValueError as e:
                    messagebox.showerror("Error", f"Error en verificación de contraseña: {e}")
            elif soporte:  # Si es un empleado de soporte
                # Verificar si la contraseña coincide con la almacenada en la base de datos
                try:
                    # La contraseña está almacenada como string, necesitamos convertirla a bytes
                    hash_almacenado = soporte[1].encode('utf-8')
                    if bcrypt.checkpw(contraseña.encode('utf-8'), hash_almacenado):
                        ventana_login.destroy()  # Cerrar ventana de login
                        mostrar_menu_soporte(correo)  # Pasar el correo como referencia
                    else:
                        messagebox.showerror("Error", "Correo o contraseña incorrectos.")
                except ValueError as e:
                    messagebox.showerror("Error", f"Error en verificación de contraseña: {e}")
            else:
                messagebox.showerror("Error", "Correo o contraseña incorrectos.")
            
            conn.close()

    # Botón de Login
    btn_login = tk.Button(ventana_login, text="Iniciar Sesión", command=verificar_usuario_interno)
    btn_login.pack(pady=20)

    ventana_login.mainloop()

def cerrar_sesion(ventana_actual):
    # Función para cerrar la sesión y volver al login
    ventana_actual.destroy()
    mostrar_login()

def mostrar_menu_cliente(correo_usuario):
    ventana_cliente = tk.Tk()
    ventana_cliente.title("Menú Cliente")

    label = tk.Label(ventana_cliente, text="Bienvenido Cliente", font=("Arial", 14))
    label.pack(pady=20)

    # Botón para generar un reporte
    btn_reportar = tk.Button(ventana_cliente, text="Generar Reporte", 
                            command=lambda: generar_reporte_cliente(correo_usuario))
    btn_reportar.pack(pady=10)

    # Botón para ver reportes enviados
    btn_ver_reportes = tk.Button(ventana_cliente, text="Ver Reportes Enviados", 
                                command=lambda: ver_reportes_enviados_cliente(correo_usuario, ventana_cliente))
    btn_ver_reportes.pack(pady=10)

    # Botón para cerrar sesión
    btn_cerrar_sesion = tk.Button(ventana_cliente, text="Cerrar Sesión", 
                                 command=lambda: cerrar_sesion(ventana_cliente),
                                 bg="red", fg="white")
    btn_cerrar_sesion.pack(pady=20)

    ventana_cliente.mainloop()

def generar_reporte_cliente(correo_usuario):
    # Aquí iría la lógica para que el cliente pueda generar un reporte
    messagebox.showinfo("Generar Reporte", "Esta función permitirá al cliente generar un reporte.")

def ver_reportes_enviados_cliente(correo_usuario, ventana_padre):
    conn = conectar_db()
    if conn:
        cursor = conn.cursor()
        # Consulta para obtener los reportes enviados por el cliente
        query_reportes_cliente = """
        SELECT titulo, descripcion, tipo_error, fecha_reporte 
        FROM Reporte 
        WHERE id_cliente = (SELECT id_cliente FROM Cliente WHERE correo = %s);
        """
        cursor.execute(query_reportes_cliente, (correo_usuario,))
        reportes = cursor.fetchall()

        if reportes:
            # Crear una nueva ventana para mostrar los reportes
            ventana_reportes = tk.Toplevel(ventana_padre)
            ventana_reportes.title("Reportes Enviados")

            label = tk.Label(ventana_reportes, text="Tus Reportes Enviados", font=("Arial", 14))
            label.pack(pady=20)

            # Mostrar cada reporte en un Label
            for reporte in reportes:
                titulo, descripcion, tipo_error, fecha_reporte = reporte
                label_reporte = tk.Label(ventana_reportes, text=f"Título: {titulo}\nDescripción: {descripcion}\nTipo: {tipo_error}\nFecha: {fecha_reporte}", font=("Arial", 10))
                label_reporte.pack(pady=10)

            # Botón para cerrar la ventana de reportes
            btn_cerrar = tk.Button(ventana_reportes, text="Cerrar", command=ventana_reportes.destroy)
            btn_cerrar.pack(pady=10)
        else:
            messagebox.showinfo("Sin Reportes", "No tienes reportes enviados.")
        
        conn.close()

def mostrar_menu_soporte(correo_usuario):
    ventana_soporte = tk.Tk()
    ventana_soporte.title("Menú Soporte")

    label = tk.Label(ventana_soporte, text="Bienvenido Soporte", font=("Arial", 14))
    label.pack(pady=20)

    # Botón para ver los reportes generados
    btn_ver_reportes = tk.Button(ventana_soporte, text="Ver Reportes", 
                                command=ver_reportes_soporte)
    btn_ver_reportes.pack(pady=10)

    # Botón para asignar un reporte
    btn_asignar_reporte = tk.Button(ventana_soporte, text="Asignar Reporte", 
                                   command=asignar_reporte)
    btn_asignar_reporte.pack(pady=10)

    # Botón para actualizar el estado de un reporte
    btn_actualizar_reporte = tk.Button(ventana_soporte, text="Actualizar Reporte", 
                                      command=actualizar_reporte)
    btn_actualizar_reporte.pack(pady=10)

    # Botón para cerrar sesión
    btn_cerrar_sesion = tk.Button(ventana_soporte, text="Cerrar Sesión", 
                                 command=lambda: cerrar_sesion(ventana_soporte),
                                 bg="red", fg="white")
    btn_cerrar_sesion.pack(pady=20)

    ventana_soporte.mainloop()

def ver_reportes_soporte():
    # Aquí iría la lógica para que el soporte pueda ver los reportes
    messagebox.showinfo("Ver Reportes", "Esta función permitirá al soporte ver los reportes generados.")

def asignar_reporte():
    # Aquí iría la lógica para asignar un reporte a un empleado de soporte
    messagebox.showinfo("Asignar Reporte", "Esta función permitirá al soporte asignar un reporte.")

def actualizar_reporte():
    # Aquí iría la lógica para actualizar el estado de un reporte
    messagebox.showinfo("Actualizar Reporte", "Esta función permitirá al soporte actualizar el estado de un reporte.")

# Iniciar la aplicación mostrando la pantalla de login
if __name__ == "__main__":
    mostrar_login()
