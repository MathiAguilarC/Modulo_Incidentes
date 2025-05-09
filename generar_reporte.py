from conexion import conectar

def crear_incidencia():
    conn = conectar()
    cursor = conn.cursor()

    print("=== CREAR NUEVA INCIDENCIA ===")
    titulo = input("Título: ")
    descripcion = input("Descripción: ")
    prioridad = input("Prioridad (Alta, Media, Baja): ")
    categoria = input("Categoría: ")
    id_usuario = int(input("ID del usuario que reporta: "))

    # Aquí se cambia de 'incidencias' a 'incidencia'
    sql = '''
    INSERT INTO incidencia (titulo, descripcion, prioridad, categoria, estado, id_usuario, fecha_creacion)
    VALUES (%s, %s, %s, %s, 'Pendiente', %s, NOW())
    '''
    valores = (titulo, descripcion, prioridad, categoria, id_usuario)

    cursor.execute(sql, valores)
    conn.commit()

    print("✅ Incidencia registrada exitosamente.")
    conn.close()

if __name__ == "__main__":
    crear_incidencia()
