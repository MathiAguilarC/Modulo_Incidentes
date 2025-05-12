package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class test5 {

    private ProductoRepository repo;
    private ProductoController ctrl;

    @BeforeEach
    void setUp() {
        repo = mock(ProductoRepository.class);
        ctrl = new ProductoController(repo);
    }

    // 1) CREACIÓN (POST) ADICIONAL
    @Test
    void crearProducto_cuerpoNull_lanzaNPE() {
        assertThrows(NullPointerException.class, () -> ctrl.crearProducto(null));
    }

//    @Test
//    void crearProducto_saveDevuelveNull_lanzaNPE() {
//        Producto in = new Producto("X", 1.0);
//        when(repo.save(in)).thenReturn(null);
//
//        assertThrows(NullPointerException.class, () -> ctrl.crearProducto(in));
//    }

    @Test
    void crearProducto_precioNegativeZero_rechazado() {
        Producto in = new Producto("Z", -0.0);
        ResponseEntity<?> r = ctrl.crearProducto(in);
        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        assertTrue(r.getBody().toString().contains("precio"));
        verify(repo, never()).save(any());
    }

    @Test
    void crearProducto_saveDevuelveNull_devuelve201ConBodyNull() {
        Producto in = new Producto("X", 1.0);
        when(repo.save(in)).thenReturn(null);

        ResponseEntity<?> response = ctrl.crearProducto(in);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    void crearProducto_precioNegativeInfinity_rechazado() {
        Producto in = new Producto("NegInf", Double.NEGATIVE_INFINITY);
        ResponseEntity<?> r = ctrl.crearProducto(in);
        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        verify(repo, never()).save(any());
    }

    // 2) LISTADO (GET) ADICIONAL
    @Test
    void listarDevuelveListaModificable_porDefecto() {
        when(repo.findAll()).thenReturn(new java.util.ArrayList<>());
        assertDoesNotThrow(() -> ctrl.listarProductos().add(new Producto("A",1.0)));
    }

    // 3) ACTUALIZACIÓN (PUT) – CP-06 a CP-07
    @Test
    void actualizarProducto_valido_retornaOK() {
        Long id = 2L;
        Producto nuevo = new Producto("Nuevo", 5.0);
        Producto viejo = new Producto("Viejo", 3.0);
        ReflectionTestUtils.setField(viejo, "id", id);

        when(repo.existsById(id)).thenReturn(true);
        when(repo.findById(id)).thenReturn(Optional.of(viejo));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> r = ctrl.actualizarProducto(id, nuevo);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        Producto updated = (Producto) r.getBody();
        assertEquals(id, updated.getId());
        assertEquals("Nuevo", updated.getNombre());
        assertEquals(5.0, updated.getPrecio());
    }

    @Test
    void actualizarProducto_bodyNull_lanzaNPE() {
        assertThrows(NullPointerException.class, () -> ctrl.actualizarProducto(1L, null));
    }
    //ERROR ENCONTRADO
    @Test
    void actualizarProducto_precioInfinito_rechazado() {
        Long id = 3L;
        Producto nuevo = new Producto("X", Double.POSITIVE_INFINITY);

        when(repo.existsById(id)).thenReturn(true);
        ResponseEntity<?> r = ctrl.actualizarProducto(id, nuevo);

        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        verify(repo, never()).findById(any());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizarProducto_idNoExiste_retorna404() {
        Long id = 99L;
        Producto nuevo = new Producto("X", 1.0);
        when(repo.existsById(id)).thenReturn(false);

        ResponseEntity<?> r = ctrl.actualizarProducto(id, nuevo);
        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
        verify(repo, never()).findById(any());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizarProducto_findByIdEmpty_lanzaNoSuchElement() {
        Long id = 5L;
        Producto nuevo = new Producto("A",2.0);
        when(repo.existsById(id)).thenReturn(true);
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> ctrl.actualizarProducto(id, nuevo));
    }

    // 4) ELIMINACIÓN (DELETE) – CP-08 a CP-09
    @Test
    void eliminarProducto_existente_retornaOK() {
        Long id = 4L;
        when(repo.existsById(id)).thenReturn(true);

        ResponseEntity<?> r = ctrl.eliminarProducto(id);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertTrue(r.getBody().toString().contains("exitosamente"));
        verify(repo).deleteById(id);
    }

    @Test
    void eliminarProducto_noExistente_retorna404() {
        Long id = 77L;
        when(repo.existsById(id)).thenReturn(false);

        ResponseEntity<?> r = ctrl.eliminarProducto(id);
        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
        verify(repo, never()).deleteById(any());
    }

    @Test
    void eliminarProducto_deleteByIdFalla_lanzaExcepcion() {
        Long id = 6L;
        when(repo.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("BD error")).when(repo).deleteById(id);

        assertThrows(RuntimeException.class, () -> ctrl.eliminarProducto(id));
    }
}
