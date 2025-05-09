from conexion import conectar

def crear_incidencia():
    conn = conectar()
    if not conn:
        return
    cursor = conn.cursor()

    print("=== CREAR NUEVA INCIDENCIA ===")
    titulo = input("Título: ")
    descripcion = input("Descripción: ")
    prioridad = input("Prioridad (Alta, Media, Baja): ")
    categoria = input("Categoría: ")
    id_usuario = int(input("ID del usuario que reporta: "))

    sql = '''
    INSERT INTO Incidencia (titulo, descripcion, prioridad, categoria, estado, id_usuario, fecha_creacion)
    VALUES (%s, %s, %s, %s, 'Pendiente', %s, NOW())
    '''
    valores = (titulo, descripcion, prioridad, categoria, id_usuario)

    try:
        cursor.execute(sql, valores)
        conn.commit()
        print("✅ Incidencia registrada exitosamente.")
    except Exception as e:
        print(f"Error al registrar incidencia: {e}")
    finally:
        conn.close()

if __name__ == "__main__":
    crear_incidencia()
