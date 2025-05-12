package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class test4 {

    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoController = new ProductoController(productoRepository);
    }

    // Test para el bug identificado en CP03_02
    @Test
    void CP_BG01_bugEnActualizacionConNombreInvalido() {
        // Arrange
        Long id = 1L;
        Producto productoNuevo = new Producto("", 10.0);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre no puede estar vacío.", response.getBody());

        // Al validar el nombre antes de cualquier acceso a BD, no debe haber llamadas
        verifyNoInteractions(productoRepository);
    }


    // Test para el bug identificado en CP03_03
    @Test
    void CP_BG02_bugEnActualizacionConPrecioInvalido() {
        // Arrange
        Long id = 1L;
        Producto productoNuevo = new Producto("Producto", -5.0);

        when(productoRepository.existsById(id)).thenReturn(true);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        // Verifica que existsById se llamó pero no se realizaron más interacciones
        verifyNoInteractions(productoRepository);

    }

    // Test para verificar bug en actualización cuando id no existe
    @Test
    void CP_BG03_bugEnActualizacionConIdInexistente() {
        // Arrange
        Long id = 999L;
        Producto productoNuevo = new Producto("Producto Nuevo", 10.0);

        when(productoRepository.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado.", response.getBody());

        // Verifica que solo se llamó a existsById
        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, never()).findById(any());
        verify(productoRepository, never()).save(any());
    }

    // Test para bug potencial: actualización con nombre nulo
    @Test
    void CP_BG04_bugEnActualizacionConNombreNulo() {
        Long id = 1L;
        Producto productoNuevo = new Producto(null, 10.0);

        // No stub de existsById porque no se llama en esta ruta

        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre no puede estar vacío.", response.getBody());

        // Verifica que no interactuaste con el repo
        verifyNoInteractions(productoRepository);
    }


    // Test para bug potencial: actualización con precio nulo
    @Test
    void CP_BG05_bugEnActualizacionConPrecioNulo() {
        // Arrange
        Long id = 1L;
        Producto productoNuevo = new Producto("Producto", null);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        // Ya que la validación ocurre antes de existsById, no debe haber llamada al repo
        verifyNoInteractions(productoRepository);
    }


    // Test para verificar si hay manipulación indebida del ID al actualizar
    @Test
    void CP_BG06_noDeberiaModificarElIdAlActualizar() {
        // Arrange
        Long id = 1L;
        // 1) Instancia “real” y le inyectamos el id vía reflexión
        Producto productoExistente = new Producto("Producto Original", 5.0);
        ReflectionTestUtils.setField(productoExistente, "id", id);

        Producto productoNuevo = new Producto("Producto Actualizado", 10.0);

        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id))
                .thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(captor.capture());
        Producto guardado = captor.getValue();

        // Compruebo que el id no cambió y los campos sí
        assertEquals(id, guardado.getId());
        assertEquals("Producto Actualizado", guardado.getNombre());
        assertEquals(10.0, guardado.getPrecio());
    }


    // Test para verificar comportamiento con precio cero al actualizar
    @Test
    void CP_BG07_deberiaRechazarActualizacionConPrecioCero() {
        // Arrange
        Long id = 1L;
        Producto productoNuevo = new Producto("Producto", 0.0);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoNuevo);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        // Como la validación ocurre antes de existsById, no debe llamar al repositorio en absoluto
        verifyNoInteractions(productoRepository);
    }

}