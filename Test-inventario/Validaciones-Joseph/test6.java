package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class test6 {
    //SI HAY ERROR
    //ProductoBoundaryTest
    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoController = new ProductoController(productoRepository);
    }

    // Pruebas de valores límite para nombre
    @Test
    void CP_BT01_deberiaAceptarNombreConEspacios() {
        // Arrange
        Producto producto = new Producto("  Producto con espacios  ", 10.0);
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP_BT02_deberiaRechazarNombreSoloEspacios() {
        // Arrange
        Producto producto = new Producto("     ", 10.0);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", response.getBody());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void CP_BT03_deberiaAceptarNombreMuyLargo() {
        // Arrange
        String nombreLargo = "a".repeat(1000); // Nombre extremadamente largo
        Producto producto = new Producto(nombreLargo, 10.0);
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    // Pruebas de valores límite para precio
    @Test
    void CP_BT04_deberiaAceptarPrecioMuyPequeno() {
        // Arrange
        Double precioMinimo = 0.000001; // Precio muy pequeño pero positivo
        Producto producto = new Producto("Producto barato", precioMinimo);
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP_BT05_deberiaAceptarPrecioMuyGrande() {
        // Arrange
        Double precioMaximo = Double.MAX_VALUE; // Precio extremadamente grande
        Producto producto = new Producto("Producto caro", precioMaximo);
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP_BT06_deberiaRechazarPrecioInfinitamenteGrande() {
        // Arrange
        Double precioInfinito = Double.POSITIVE_INFINITY;
        Producto producto = new Producto("Producto infinito", precioInfinito);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("precio"));
        verify(productoRepository, never()).save(any());
    }

    // Casos límite para actualización
    @Test
    void CP_BT07_deberiaActualizarSoloNombre() {
        // Arrange
        Long id = 1L;
        Producto productoExistente = new Producto("Nombre original", 10.0);
        Producto productoActualizado = new Producto("Nombre actualizado", 10.0); // Mismo precio
        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(productoActualizado);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoActualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Producto resultado = (Producto) response.getBody();
        assertEquals("Nombre actualizado", resultado.getNombre());
        assertEquals(10.0, resultado.getPrecio());
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP_BT08_deberiaActualizarSoloPrecio() {
        // Arrange
        Long id = 1L;
        Producto productoExistente = new Producto("Producto", 10.0);
        Producto productoActualizado = new Producto("Producto", 20.0); // Mismo nombre
        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(productoActualizado);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoActualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Producto resultado = (Producto) response.getBody();
        assertEquals("Producto", resultado.getNombre());
        assertEquals(20.0, resultado.getPrecio());
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    // Verificación del comportamiento con ids límite
    @Test
    void CP_BT09_deberiaRechazarActualizacionConIdNegativo() {
        // Arrange
        Long idNegativo = -1L;
        Producto producto = new Producto("Producto", 10.0);
        when(productoRepository.existsById(idNegativo)).thenReturn(false);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(idNegativo, producto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado.", response.getBody());
        verify(productoRepository, times(1)).existsById(idNegativo);
        verify(productoRepository, never()).findById(any());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void CP_BT10_deberiaRechazarEliminacionConIdCero() {
        // Arrange
        Long idCero = 0L;
        when(productoRepository.existsById(idCero)).thenReturn(false);

        // Act
        ResponseEntity<?> response = productoController.eliminarProducto(idCero);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado.", response.getBody());
        verify(productoRepository, times(1)).existsById(idCero);
        verify(productoRepository, never()).deleteById(any());
    }
}