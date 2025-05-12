package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.http.ResponseEntity;

public class ProductoControllerTest {

    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoController = new ProductoController(productoRepository);
    }

    @Test
    void deberiaRechazarProductoConNombreVacio() {
        Producto producto = new Producto("   ", 10.0);
        ResponseEntity<?> response = productoController.crearProducto(producto);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", response.getBody());
    }

    @Test
    void deberiaGuardarProductoValido() {
        Producto producto = new Producto("Lapicero", 2.5);
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(producto);

        ResponseEntity<?> response = productoController.crearProducto(producto);

        assertEquals(201, response.getStatusCodeValue()); // Se puede ajustar si usas CREATED
        Producto guardado = (Producto) response.getBody();
        assertNotNull(guardado);
        assertEquals("Lapicero", guardado.getNombre());
        assertEquals(2.5, guardado.getPrecio());
    }

    @Test
    void deberiaRechazarPrecioInvalido() {
        Producto producto = new Producto("Corrector", -1.0);
        ResponseEntity<?> response = productoController.crearProducto(producto);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());
    }
}
