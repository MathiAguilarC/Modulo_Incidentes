package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class test3 {

    private ProductoRepository repo;
    private ProductoController ctrl;

    @BeforeEach
    void setUp() {
        repo = mock(ProductoRepository.class);
        ctrl = new ProductoController(repo);
    }

    // ——————————————
    // Escenarios de creación EXTRA
    // ——————————————

    // CP-01 ext: nombre con espacios en los bordes (debe aceptarlo tal cual)
    @Test
    void deberiaGuardarNombreConEspaciosEnBordes() {
        Producto in = new Producto("  Lapicero Azul  ", 3.5);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(201, resp.getStatusCodeValue());
        Producto out = (Producto) resp.getBody();
        assertEquals("  Lapicero Azul  ", out.getNombre());
        assertEquals(3.5, out.getPrecio());
    }

    // CP-01 ext: nombre con caracteres especiales y números
    @Test
    void deberiaGuardarNombreConCaracteresEspeciales() {
        Producto in = new Producto("Café-Máquina #1", 100.0);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(201, resp.getStatusCodeValue());
        Producto out = (Producto) resp.getBody();
        assertEquals("Café-Máquina #1", out.getNombre());
    }

    // CP-01 ext: nombre muy largo (500 caracteres)
    @Test
    void deberiaAceptarNombreMuyLargo() {
        String muyLargo = "A".repeat(500);
        Producto in = new Producto(muyLargo, 10.0);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(muyLargo, ((Producto) resp.getBody()).getNombre());
    }

    // CP-01 ext: precio con alta precisión decimal
    @Test
    void deberiaGuardarPrecioAltaPrecision() {
        Producto in = new Producto("Tijera", 0.009);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(0.009, ((Producto) resp.getBody()).getPrecio());
    }

    // CP-01 ext: precio muy grande
    @Test
    void deberiaAceptarPrecioMuyGrande() {
        Producto in = new Producto("Monitor", 1e6);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(1e6, ((Producto) resp.getBody()).getPrecio());
    }

    // ——————————————
    // Escenarios de fallo en creación
    // ——————————————

    // Simular fallo en repository.save → propagación de excepción
    @Test
    void deberiaLanzarCuandoSaveFalla() {
        Producto in = new Producto("Lapicero", 2.5);
        when(repo.save(any())).thenThrow(new RuntimeException("Error BD"));

        assertThrows(RuntimeException.class, () -> ctrl.crearProducto(in));
    }

    // ——————————————
    // Escenarios de listado EXTRA
    // ——————————————

    // CP-04 ext: listado nulo del repositorio → lanzar excepción
    @Test
    void listarDevuelveExcepcionCuandoFindAllFalla() {
        when(repo.findAll()).thenThrow(new RuntimeException("Error BD"));

        assertThrows(RuntimeException.class, () -> ctrl.listarProductos());
    }

    // CP-05 ext: respetar orden de inserción
    @Test
    void listarMantieneOrdenDeInsercion() {
        Producto p1 = new Producto("A", 1.0);
        Producto p2 = new Producto("B", 2.0);
        Producto p3 = new Producto("C", 3.0);
        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        List<Producto> lista = ctrl.listarProductos();
        assertEquals(3, lista.size());
        assertEquals("A", lista.get(0).getNombre());
        assertEquals("B", lista.get(1).getNombre());
        assertEquals("C", lista.get(2).getNombre());
    }

    // CP-05 ext: listar con un solo elemento
    @Test
    void listarConUnSoloElemento() {
        Producto p = new Producto("Único", 9.9);
        when(repo.findAll()).thenReturn(Collections.singletonList(p));

        List<Producto> lista = ctrl.listarProductos();
        assertEquals(1, lista.size());
        assertEquals("Único", lista.get(0).getNombre());
    }
}
