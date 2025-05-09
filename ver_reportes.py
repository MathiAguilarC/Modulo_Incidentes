from conexion import conectar

def ver_incidencia():
    conn = conectar()
    cursor = conn.cursor()

    print("=== LISTA DE INCIDENCIAS PENDIENTES ===")
    cursor.execute("SELECT id, titulo, estado FROM incidencia WHERE estado = 'Pendiente'")
    incidencia = cursor.fetchall()

    if not incidencia:
        print("⚠️ No hay incidencias pendientes.")
        conn.close()
        return

    for inc in incidencia:
        print(f"ID: {inc[0]} | Título: {inc[1]} | Estado: {inc[2]}")

    id_buscar = int(input("\nIngresa el ID de la incidencia para ver detalles: "))
    cursor.execute("SELECT * FROM incidencia WHERE id = %s", (id_buscar,))
    detalle = cursor.fetchone()

    if detalle:
        print("\n=== DETALLE DE LA INCIDENCIA ===")
        columnas = [desc[0] for desc in cursor.description]
        for i in range(len(detalle)):
            print(f"{columnas[i]}: {detalle[i]}")
    else:
        print("⚠️ No se encontró esa incidencia.")

    conn.close()

if __name__ == "__main__":
    ver_incidencia()


