import tkinter as tk
from tkinter import messagebox, ttk, simpledialog
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
    ventana_login.geometry("400x300")

    # Etiquetas y campos de entrada
    label_correo = tk.Label(ventana_login, text="Correo Electrónico")
    label_correo.pack(pady=5)
    entry_correo = tk.Entry(ventana_login, width=30)
    entry_correo.pack(pady=5)

    label_contraseña = tk.Label(ventana_login, text="Contraseña")
    label_contraseña.pack(pady=5)
    entry_contraseña = tk.Entry(ventana_login, show="*", width=30)
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
    ventana_cliente.geometry("500x400")

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
    """
    Función para generar un reporte nuevo desde la interfaz del cliente.
    Tras generar el reporte, crea automáticamente una incidencia relacionada.
    """
    # Crear una nueva ventana para el formulario
    ventana_reporte = tk.Toplevel()
    ventana_reporte.title("Generar Nuevo Reporte")
    ventana_reporte.geometry("500x400")
    
    # Título del formulario
    tk.Label(ventana_reporte, text="NUEVO REPORTE", font=("Arial", 14, "bold")).pack(pady=10)
    
    # Frame para el formulario
    frame_form = tk.Frame(ventana_reporte)
    frame_form.pack(fill="both", expand=True, padx=20, pady=10)
    
    # Campos del formulario
    tk.Label(frame_form, text="Título:", anchor="w").pack(fill="x", pady=2)
    entry_titulo = tk.Entry(frame_form, width=50)
    entry_titulo.pack(fill="x", pady=5)
    
    tk.Label(frame_form, text="Descripción:", anchor="w").pack(fill="x", pady=2)
    text_descripcion = tk.Text(frame_form, height=5, width=50)
    text_descripcion.pack(fill="x", pady=5)
    
    tk.Label(frame_form, text="Tipo de Error:", anchor="w").pack(fill="x", pady=2)
    
    # Variable para almacenar la opción seleccionada
    tipo_error = tk.StringVar(value="producto")  # Valor por defecto
    
    # Opciones de tipo de error según la BD
    frame_tipos = tk.Frame(frame_form)
    frame_tipos.pack(fill="x", pady=5)
    
    tipos = [("Pago", "pago"), ("Devolución", "devolucion"), 
             ("Producto", "producto"), ("Otro", "otro")]
    
    for texto, valor in tipos:
        tk.Radiobutton(frame_tipos, text=texto, variable=tipo_error, 
                    value=valor).pack(side="left", padx=10)
    
    # Función para guardar el reporte
    def guardar_reporte():
        # Validar campos
        titulo = entry_titulo.get().strip()
        descripcion = text_descripcion.get("1.0", "end-1c").strip()
        tipo = tipo_error.get()
        
        if not titulo or not descripcion:
            messagebox.showerror("Error", "Por favor complete todos los campos.")
            return
        
        # Obtener conexión a la BD
        conn = conectar_db()
        if conn:
            try:
                cursor = conn.cursor()
                
                # Obtener el ID del cliente desde su correo
                cursor.execute("SELECT id_cliente FROM Cliente WHERE correo = %s", (correo_usuario,))
                cliente = cursor.fetchone()
                
                if not cliente:
                    messagebox.showerror("Error", "No se pudo identificar al cliente.")
                    conn.close()
                    return
                
                id_cliente = cliente[0]
                
                # Insertar el reporte
                cursor.execute("""
                    INSERT INTO Reporte (id_cliente, titulo, descripcion, tipo_error)
                    VALUES (%s, %s, %s, %s) RETURNING id_reporte
                """, (id_cliente, titulo, descripcion, tipo))
                
                # Obtener el ID del reporte recién creado
                id_reporte = cursor.fetchone()[0]
                
                # Determinar la prioridad según el tipo de error
                prioridad = "Alta" if tipo in ["pago", "devolucion"] else "Media"
                
                # Crear la incidencia automáticamente
                cursor.execute("""
                    INSERT INTO Incidencia (id_reporte, estado, prioridad, fecha_reporte)
                    VALUES (%s, 'Pendiente', %s, CURRENT_TIMESTAMP)
                """, (id_reporte, prioridad))
                
                # Confirmar transacción
                conn.commit()
                messagebox.showinfo("Éxito", "Reporte creado exitosamente. Se ha generado una incidencia con prioridad " + prioridad)
                
                # Cerrar ventana de reporte
                ventana_reporte.destroy()
                
            except Exception as e:
                # Mostrar error
                messagebox.showerror("Error", f"No se pudo crear el reporte: {str(e)}")
                conn.rollback()
            finally:
                # Cerrar conexión
                conn.close()
    
    # Botones
    frame_botones = tk.Frame(ventana_reporte)
    frame_botones.pack(fill="x", pady=20, padx=20)
    
    tk.Button(frame_botones, text="Cancelar", 
              command=ventana_reporte.destroy).pack(side="left", padx=10)
    
    tk.Button(frame_botones, text="Guardar", bg="#4CAF50", fg="white",
              command=guardar_reporte).pack(side="right", padx=10)

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
            ventana_reportes.geometry("600x400")

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
    ventana_soporte.geometry("500x400")

    label = tk.Label(ventana_soporte, text="Bienvenido Soporte", font=("Arial", 14))
    label.pack(pady=20)

    # Botón para ver los reportes generados
    btn_ver_reportes = tk.Button(ventana_soporte, text="Ver Reportes", 
                                command=lambda: ver_reportes_soporte(ventana_soporte))
    btn_ver_reportes.pack(pady=10)

    # Botón para ver incidencias pendientes
    btn_ver_incidencias = tk.Button(ventana_soporte, text="Ver Incidencias Pendientes", 
                                command=lambda: ver_incidencias(ventana_soporte))
    btn_ver_incidencias.pack(pady=10)

    # Botón para asignar un reporte
    btn_asignar_reporte = tk.Button(ventana_soporte, text="Asignar Reporte", 
                                command=asignar_reporte)
    btn_asignar_reporte.pack(pady=10)

    # Botón para actualizar el estado de un reporte
    btn_actualizar_reporte = tk.Button(ventana_soporte, text="Actualizar Reporte", command=actualizar_reporte)
    btn_actualizar_reporte.pack(pady=10)

    # Botón para cerrar sesión
    btn_cerrar_sesion = tk.Button(ventana_soporte, text="Cerrar Sesión", 
    command=lambda: cerrar_sesion(ventana_soporte),
    bg="red", fg="white")
    btn_cerrar_sesion.pack(pady=20)

    ventana_soporte.mainloop()

def ver_reportes_soporte(ventana_padre):
    # Mostrar los reportes disponibles
    conn = conectar_db()
    if conn:
        cursor = conn.cursor()
        
        # Consulta para obtener todos los reportes
        cursor.execute("""
            SELECT r.id_reporte, c.nombre, r.titulo, r.tipo_error, r.fecha_reporte 
            FROM Reporte r
            JOIN Cliente c ON r.id_cliente = c.id_cliente
            ORDER BY r.fecha_reporte DESC
        """)
        reportes = cursor.fetchall()
        
        if reportes:
            # Crear una nueva ventana para mostrar los reportes
            ventana_reportes = tk.Toplevel(ventana_padre)
            ventana_reportes.title("Reportes Recibidos")
            ventana_reportes.geometry("800x500")
            
            # Frame para la tabla
            frame = tk.Frame(ventana_reportes)
            frame.pack(fill="both", expand=True, padx=20, pady=20)
            
            # Crear un Treeview (tabla)
            tabla = ttk.Treeview(frame, columns=("ID", "Cliente", "Título", "Tipo", "Fecha"), show="headings")
            tabla.heading("ID", text="ID")
            tabla.heading("Cliente", text="Cliente")
            tabla.heading("Título", text="Título")
            tabla.heading("Tipo", text="Tipo de Error")
            tabla.heading("Fecha", text="Fecha")
            
            # Establecer anchos de columnas
            tabla.column("ID", width=50)
            tabla.column("Cliente", width=150)
            tabla.column("Título", width=200)
            tabla.column("Tipo", width=100)
            tabla.column("Fecha", width=150)
            
            # Insertar datos en la tabla
            for reporte in reportes:
                id_reporte, cliente, titulo, tipo, fecha = reporte
                tabla.insert("", "end", values=(id_reporte, cliente, titulo, tipo, fecha))
            
            tabla.pack(fill="both", expand=True)
            
            # Función para mostrar detalles al hacer doble clic
            def mostrar_detalle_reporte(event):
                seleccion = tabla.selection()
                if seleccion:  # Si hay una fila seleccionada
                    item = tabla.item(seleccion[0])
                    id_reporte = item["values"][0]
                    
                    # Consultar detalles del reporte
                    cursor.execute("""
                        SELECT r.*, c.nombre as nombre_cliente
                        FROM Reporte r
                        JOIN Cliente c ON r.id_cliente = c.id_cliente
                        WHERE r.id_reporte = %s
                    """, (id_reporte,))
                    
                    detalle = cursor.fetchone()
                    if detalle:
                        # Obtener los nombres de las columnas
                        columnas = [desc[0] for desc in cursor.description]
                        
                        # Crear ventana de detalles
                        ventana_detalle = tk.Toplevel(ventana_reportes)
                        ventana_detalle.title(f"Detalle del Reporte #{id_reporte}")
                        ventana_detalle.geometry("500x400")
                        
                        # Mostrar detalles en la ventana
                        tk.Label(ventana_detalle, text=f"Detalle del Reporte #{id_reporte}", font=("Arial", 14)).pack(pady=10)
                        
                        # Frame para los detalles
                        frame_detalle = tk.Frame(ventana_detalle)
                        frame_detalle.pack(fill="both", expand=True, padx=20, pady=10)
                        
                        # Mostrar cada campo
                        for i in range(len(detalle)):
                            if columnas[i] != "id_cliente":  # No mostrar id_cliente ya que mostramos nombre_cliente
                                tk.Label(frame_detalle, text=f"{columnas[i]}: {detalle[i]}", anchor="w", justify="left").pack(fill="x", pady=2)
                        
                        # Botón para cerrar
                        tk.Button(ventana_detalle, text="Cerrar", command=ventana_detalle.destroy).pack(pady=10)
            
            # Vincular doble clic a la función de mostrar detalles
            tabla.bind("<Double-1>", mostrar_detalle_reporte)
            
            # Botón para cerrar
            tk.Button(ventana_reportes, text="Cerrar", command=ventana_reportes.destroy).pack(pady=10)
        else:
            messagebox.showinfo("Sin Reportes", "No hay reportes en el sistema.")
        
        conn.close()

def ver_incidencias(ventana_padre):
    """
    Muestra las incidencias pendientes y permite ver detalles.
    Esta función integra la funcionalidad de ver_incidencia.py
    """
    conn = conectar_db()
    if conn:
        cursor = conn.cursor()
        
        # Crear una nueva ventana para mostrar las incidencias
        ventana_incidencias = tk.Toplevel(ventana_padre)
        ventana_incidencias.title("Incidencias Pendientes")
        ventana_incidencias.geometry("800x500")
        
        # Label título
        tk.Label(ventana_incidencias, text="LISTA DE INCIDENCIAS PENDIENTES", font=("Arial", 14, "bold")).pack(pady=10)
        
        # Frame para la tabla
        frame = tk.Frame(ventana_incidencias)
        frame.pack(fill="both", expand=True, padx=20, pady=20)
        
        # Consultar incidencias pendientes
        cursor.execute("SELECT id_incidencia, id_reporte, prioridad, fecha_reporte FROM incidencia WHERE estado = 'Pendiente'")
        incidencias = cursor.fetchall()
        
        if not incidencias:
            tk.Label(frame, text="⚠️ No hay incidencias pendientes.", font=("Arial", 12)).pack(pady=20)
        else:
            # Crear un Treeview (tabla)
            tabla = ttk.Treeview(frame, columns=("ID", "ID Reporte", "Prioridad", "Fecha"), show="headings")
            tabla.heading("ID", text="ID")
            tabla.heading("ID Reporte", text="ID Reporte")
            tabla.heading("Prioridad", text="Prioridad")
            tabla.heading("Fecha", text="Fecha")
            
            # Establecer anchos de columnas
            tabla.column("ID", width=50)
            tabla.column("ID Reporte", width=100)
            tabla.column("Prioridad", width=100)
            tabla.column("Fecha", width=150)
            
            # Insertar datos en la tabla
            for inc in incidencias:
                id_inc, id_reporte, prioridad, fecha = inc
                tabla.insert("", "end", values=(id_inc, id_reporte, prioridad, fecha))
            
            tabla.pack(fill="both", expand=True)
            
            # Frame para entrada de ID y botón
            frame_buscar = tk.Frame(ventana_incidencias)
            frame_buscar.pack(fill="x", padx=20, pady=10)
            
            tk.Label(frame_buscar, text="Ingresa el ID de la incidencia para ver detalles:").pack(side="left", padx=5)
            entry_id = tk.Entry(frame_buscar, width=10)
            entry_id.pack(side="left", padx=5)
            
            def buscar_detalle():
                try:
                    id_buscar = int(entry_id.get())
                    cursor.execute("SELECT * FROM incidencia WHERE id_incidencia = %s", (id_buscar,))
                    detalle = cursor.fetchone()
                    
                    if detalle:
                        # Obtener nombres de columnas
                        columnas = [desc[0] for desc in cursor.description]
                        
                        # Crear ventana de detalles
                        ventana_detalle = tk.Toplevel(ventana_incidencias)
                        ventana_detalle.title(f"Detalle de Incidencia #{id_buscar}")
                        ventana_detalle.geometry("500x400")
                        
                        # Título
                        tk.Label(ventana_detalle, text=f"DETALLE DE LA INCIDENCIA #{id_buscar}", font=("Arial", 14, "bold")).pack(pady=10)
                        
                        # Frame para los detalles
                        frame_detalle = tk.Frame(ventana_detalle)
                        frame_detalle.pack(fill="both", expand=True, padx=20, pady=10)
                        
                        # Mostrar cada campo
                        for i in range(len(detalle)):
                            tk.Label(frame_detalle, text=f"{columnas[i]}: {detalle[i]}", anchor="w", justify="left").pack(fill="x", pady=2)
                        
                        # Botón para cerrar
                        tk.Button(ventana_detalle, text="Cerrar", command=ventana_detalle.destroy).pack(pady=10)
                    else:
                        messagebox.showwarning("No encontrado", "⚠️ No se encontró esa incidencia.")
                except ValueError:
                    messagebox.showerror("Error", "Por favor, ingrese un ID válido (número entero).")
            
            # Botón para buscar
            tk.Button(frame_buscar, text="Ver Detalles", command=buscar_detalle).pack(side="left", padx=5)
            
        # Botón para cerrar
        tk.Button(ventana_incidencias, text="Cerrar", command=ventana_incidencias.destroy).pack(pady=10)
        
        conn.close()

def asignar_reporte():
    conn = conectar_db()
    if not conn:
        return
    cursor = conn.cursor()

    # Obtener incidencias sin empleado asignado
    cursor.execute("""
        SELECT i.id_incidencia, r.titulo, i.estado
        FROM Incidencia i
        LEFT JOIN Reporte r ON i.id_reporte = r.id_reporte
        WHERE i.id_empleado_soporte IS NULL
    """)
    incidencias = cursor.fetchall()

    if not incidencias:
        messagebox.showinfo("Asignar Reporte", "No hay incidencias sin empleado asignado.")
        conn.close()
        return

    # Obtener empleados de soporte
    cursor.execute("SELECT id_empleado, nombre FROM EmpleadoSoporte")
    empleados = cursor.fetchall()

    if not empleados:
        messagebox.showinfo("Asignar Reporte", "No hay empleados de soporte disponibles.")
        conn.close()
        return

    # Crear ventana para asignar
    ventana_asignar = tk.Toplevel()
    ventana_asignar.title("Asignar Empleado a Incidencia")

    label_inc = tk.Label(ventana_asignar, text="Selecciona una incidencia sin asignar:")
    label_inc.pack(pady=5)

    tree_inc = ttk.Treeview(ventana_asignar, columns=("ID", "Título", "Estado"), show="headings")
    tree_inc.heading("ID", text="ID")
    tree_inc.heading("Título", text="Título")
    tree_inc.heading("Estado", text="Estado")
    tree_inc.pack(pady=5)

    for inc in incidencias:
        tree_inc.insert("", "end", values=inc)

    label_emp = tk.Label(ventana_asignar, text="Selecciona un empleado:")
    label_emp.pack(pady=5)

    tree_emp = ttk.Treeview(ventana_asignar, columns=("ID", "Nombre"), show="headings")
    tree_emp.heading("ID", text="ID")
    tree_emp.heading("Nombre", text="Nombre")
    tree_emp.pack(pady=5)

    for emp in empleados:
        tree_emp.insert("", "end", values=emp)

    def asignar():
        selected_inc = tree_inc.selection()
        selected_emp = tree_emp.selection()
        if not selected_inc or not selected_emp:
            messagebox.showerror("Error", "Debe seleccionar una incidencia y un empleado.")
            return
        id_incidencia = tree_inc.item(selected_inc[0])['values'][0]
        id_empleado = tree_emp.item(selected_emp[0])['values'][0]

        try:
            cursor.execute("""
                UPDATE Incidencia
                SET id_empleado_soporte = %s, estado = 'En Progreso', fecha_actualizacion = NOW()
                WHERE id_incidencia = %s
            """, (id_empleado, id_incidencia))
            conn.commit()
            messagebox.showinfo("Asignar Reporte", f"Empleado asignado a la incidencia {id_incidencia} exitosamente.")
            ventana_asignar.destroy()
        except Exception as e:
            messagebox.showerror("Error", f"No se pudo asignar el empleado: {e}")

    btn_asignar = tk.Button(ventana_asignar, text="Asignar", command=asignar)
    btn_asignar.pack(pady=10)
def actualizar_reporte():
    conn = conectar_db()
    if not conn:
        return
    cursor = conn.cursor()

    # Pedir correo del empleado para filtrar incidencias asignadas
    correo_empleado = simpledialog.askstring("Actualizar Reporte", "Ingrese su correo electrónico:")

    if not correo_empleado:
        messagebox.showerror("Error", "Debe ingresar un correo electrónico.")
        conn.close()
        return

    # Obtener id_empleado a partir del correo
    cursor.execute("SELECT id_empleado FROM EmpleadoSoporte WHERE correo = %s", (correo_empleado,))
    empleado = cursor.fetchone()

    if not empleado:
        messagebox.showerror("Error", "Empleado no encontrado.")
        conn.close()
        return

    id_empleado = empleado[0]

    # Obtener incidencias asignadas a este empleado
    cursor.execute("""
        SELECT i.id_incidencia, r.titulo, i.estado
        FROM Incidencia i
        JOIN Reporte r ON i.id_reporte = r.id_reporte
        WHERE i.id_empleado_soporte = %s
    """, (id_empleado,))
    incidencias = cursor.fetchall()

    if not incidencias:
        messagebox.showinfo("Actualizar Reporte", "No hay incidencias asignadas a este empleado.")
        conn.close()
        return

    ventana_actualizar = tk.Toplevel()
    ventana_actualizar.title("Actualizar Estado de Incidencia")

    label_inc = tk.Label(ventana_actualizar, text="Selecciona una incidencia para actualizar:")
    label_inc.pack(pady=5)

    tree_inc = ttk.Treeview(ventana_actualizar, columns=("ID", "Título", "Estado"), show="headings")
    tree_inc.heading("ID", text="ID")
    tree_inc.heading("Título", text="Título")
    tree_inc.heading("Estado", text="Estado")
    tree_inc.pack(pady=5)

    for inc in incidencias:
        tree_inc.insert("", "end", values=inc)

    label_estado = tk.Label(ventana_actualizar, text="Selecciona el nuevo estado:")
    label_estado.pack(pady=5)

    estados = ['Pendiente', 'En Progreso', 'Resuelto', 'Cerrado']
    combo_estado = ttk.Combobox(ventana_actualizar, values=estados, state="readonly")
    combo_estado.pack(pady=5)
    combo_estado.current(0)

    def actualizar():
        selected_inc = tree_inc.selection()
        nuevo_estado = combo_estado.get()
        if not selected_inc:
            messagebox.showerror("Error", "Debe seleccionar una incidencia.")
            return
        id_incidencia = tree_inc.item(selected_inc[0])['values'][0]

        try:
            cursor.execute("""
                UPDATE Incidencia
                SET estado = %s, fecha_actualizacion = NOW()
                WHERE id_incidencia = %s
            """, (nuevo_estado, id_incidencia))
            conn.commit()
            messagebox.showinfo("Actualizar Reporte", f"Estado actualizado a '{nuevo_estado}' para la incidencia {id_incidencia}.")
            ventana_actualizar.destroy()
        except Exception as e:
            messagebox.showerror("Error", f"No se pudo actualizar el estado: {e}")

    btn_actualizar = tk.Button(ventana_actualizar, text="Actualizar", command=actualizar)
    btn_actualizar.pack(pady=10)


# Iniciar la aplicación mostrando la pantalla de login
if __name__ == "__main__":
    mostrar_login()
