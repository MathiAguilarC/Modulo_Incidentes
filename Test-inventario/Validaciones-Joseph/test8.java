package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class test8 {

    // ProductoControllerIntegrationTest

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductoRepository productoRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/productos";
    }

    @BeforeEach
    @AfterEach
    void limpiarBaseDeDatos() {
        productoRepository.deleteAll();
    }

    @Test
    void CP_IT01_deberiaListarProductosVacio() {
        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(getBaseUrl(), List.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void CP_IT02_deberiaCrearYListarProducto() {
        // Arrange
        Producto nuevoProducto = new Producto("Test Producto", 10.0);

        // Act - Crear producto
        ResponseEntity<Producto> createResponse = restTemplate.postForEntity(
                getBaseUrl(), nuevoProducto, Producto.class);

        // Assert - Verificar creación exitosa
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("Test Producto", createResponse.getBody().getNombre());
        assertEquals(10.0, createResponse.getBody().getPrecio());

        // Act - Listar productos
        ResponseEntity<List> listResponse = restTemplate.getForEntity(getBaseUrl(), List.class);

        // Assert - Verificar lista con el producto creado
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertEquals(1, listResponse.getBody().size());
    }

    @Test
    void CP_IT03_deberiaRechazarCreacionProductoInvalido() {
        // Arrange
        Producto productoSinNombre = new Producto("", 10.0);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl(), productoSinNombre, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("nombre"));
    }

    @Test
    void CP_IT04_deberiaActualizarProductoExistente() {
        // Arrange - Crear producto inicial
        Producto productoInicial = new Producto("Producto Original", 15.0);
        ResponseEntity<Producto> createResponse = restTemplate.postForEntity(
                getBaseUrl(), productoInicial, Producto.class);
        Long id = createResponse.getBody().getId();

        // Preparar producto actualizado
        Producto productoActualizado = new Producto("Producto Actualizado", 20.0);

        // Act - Actualizar producto
        ResponseEntity<Producto> updateResponse = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(productoActualizado),
                Producto.class);

        // Assert
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(id, updateResponse.getBody().getId());
        assertEquals("Producto Actualizado", updateResponse.getBody().getNombre());
        assertEquals(20.0, updateResponse.getBody().getPrecio());
    }

    @Test
    void CP_IT05_deberiaRechazarActualizacionProductoInexistente() {
        // Arrange
        Producto productoActualizado = new Producto("Producto Actualizado", 20.0);
        Long idInexistente = 9999L;

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/" + idInexistente,
                HttpMethod.PUT,
                new HttpEntity<>(productoActualizado),
                String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("no encontrado"));
    }

    @Test
    void CP_IT06_deberiaEliminarProductoExistente() {
        // Arrange - Crear producto
        Producto producto = new Producto("Producto a Eliminar", 5.0);
        ResponseEntity<Producto> createResponse = restTemplate.postForEntity(
                getBaseUrl(), producto, Producto.class);
        Long id = createResponse.getBody().getId();

        // Act - Eliminar producto
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.DELETE,
                null,
                String.class);

        // Assert - Verificar eliminación exitosa
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertTrue(deleteResponse.getBody().contains("eliminado exitosamente"));

        // Verificar que el producto ya no existe en la base de datos
        assertFalse(productoRepository.existsById(id));
    }

    @Test
    void CP_IT07_deberiaRechazarEliminacionProductoInexistente() {
        // Arrange
        Long idInexistente = 9999L;

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/" + idInexistente,
                HttpMethod.DELETE,
                null,
                String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("no encontrado"));
    }
}