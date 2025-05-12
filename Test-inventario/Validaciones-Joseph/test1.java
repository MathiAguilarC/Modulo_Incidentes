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


public class test1 {

    private ProductoRepository repo;
    private ProductoController ctrl;

    @BeforeEach
    void setUp() {
        repo = mock(ProductoRepository.class);
        ctrl = new ProductoController(repo);
    }

    // CP-01: creación válida
    @Test
    void deberiaGuardarProductoValido() {
        Producto in = new Producto("Lapicero", 2.5);
        when(repo.save(ArgumentMatchers.any())).thenReturn(in);

        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(201, resp.getStatusCodeValue());
        Producto out = (Producto) resp.getBody();
        assertNotNull(out);
        assertEquals("Lapicero", out.getNombre());
        assertEquals(2.5, out.getPrecio());
        verify(repo).save(in);
    }

    // CP-02: nombre null
    @Test
    void deberiaRechazarNombreNull() {
        Producto in = new Producto(null, 5.0);
        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", resp.getBody());
        verify(repo, never()).save(any());
    }

    // CP-02: nombre sólo espacios
    @Test
    void deberiaRechazarNombreBlanco() {
        Producto in = new Producto("   ", 5.0);
        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", resp.getBody());
        verify(repo, never()).save(any());
    }

    // CP-03: precio null
    @Test
    void deberiaRechazarPrecioNull() {
        Producto in = new Producto("Goma", null);
        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("El precio debe ser mayor que cero.", resp.getBody());
        verify(repo, never()).save(any());
    }

    // CP-03: precio negativo
    @Test
    void deberiaRechazarPrecioNegativo() {
        Producto in = new Producto("Corrector", -1.0);
        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("El precio debe ser mayor que cero.", resp.getBody());
        verify(repo, never()).save(any());
    }

    // CP-03: precio en límite (cero)
    @Test
    void deberiaRechazarPrecioCero() {
        Producto in = new Producto("Tijera", 0.0);
        ResponseEntity<?> resp = ctrl.crearProducto(in);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("El precio debe ser mayor que cero.", resp.getBody());
        verify(repo, never()).save(any());
    }

    // CP-04: listar cuando no hay productos
    @Test
    void listarVacioDevuelveListaVacia() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        List<Producto> lista = ctrl.listarProductos();
        assertTrue(lista.isEmpty());
        verify(repo).findAll();
    }

    // CP-05: listar con varios productos
    @Test
    void listarDevuelveTodosLosProductos() {
        Producto p1 = new Producto("Lapiz", 1.0);
        Producto p2 = new Producto("Borrador", 0.5);
        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> lista = ctrl.listarProductos();
        assertEquals(2, lista.size());
        assertEquals("Lapiz", lista.get(0).getNombre());
        assertEquals("Borrador", lista.get(1).getNombre());
        verify(repo).findAll();
    }
}
