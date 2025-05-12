package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class test7 {

    private ProductoRepository repo;
    private ProductoController ctrl;

    @BeforeEach
    void setUp() {
        repo = mock(ProductoRepository.class);
        ctrl = new ProductoController(repo);
    }

    // 1) PRECIO NaN: debería devolver 400, pero devuelve 201
    @Test
    void crearPrecioNaN_comportamientoActual() {
        Producto p = new Producto("TestNaN", Double.NaN);
        ResponseEntity<?> resp = ctrl.crearProducto(p);
        // Este assert FALLARÁ porque el controlador hoy devuelve 201 Created
        assertEquals(400, resp.getStatusCodeValue(),
                "Se esperaba 400 Bad Request pero devolvió " + resp.getStatusCodeValue());
    }

    // 2) CUERPO null en POST: debería devolver 400, pero lanza NPE
    @Test
    void crearProductoNull_comportamientoActual() {
        // Este assert PASARÁ porque realmente lanza NullPointerException
        assertThrows(NullPointerException.class,
                () -> ctrl.crearProducto(null),
                "Se esperaba NullPointerException ante cuerpo null");
    }

    // 3) CUERPO null en PUT: idem POST
    @Test
    void actualizarProductoNullBody_comportamientoActual() {
        assertThrows(NullPointerException.class,
                () -> ctrl.actualizarProducto(1L, null),
                "Se esperaba NullPointerException ante body null");
    }

    // 4) EXISTS sin ENTIDAD en findById: debería devolver 404, pero lanza NoSuchElement
    @Test
    void actualizarExistsTrueFindEmpty_comportamientoActual() {
        Long id = 42L;
        when(repo.existsById(id)).thenReturn(true);
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> ctrl.actualizarProducto(id, new Producto("X", 1.0)),
                "Se esperaba NoSuchElementException debido a Optional.empty()");
    }
}
