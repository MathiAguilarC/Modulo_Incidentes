package com.example.ProyectoInventarioF2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;

public class ProductoControllerAdditionalTests {

    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoController = new ProductoController(productoRepository);
    }

    @Test
    void deberiaListarProductosVacio() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList());

        List<Producto> productos = productoController.listarProductos();

        assertNotNull(productos);
        assertTrue(productos.isEmpty());
    }

    @Test
    void deberiaListarProductosConDatos() {
        Producto p1 = new Producto("Lapicero", 1.5);
        Producto p2 = new Producto("Cuaderno", 3.0);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> productos = productoController.listarProductos();

        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertEquals("Lapicero", productos.get(0).getNombre());
        assertEquals("Cuaderno", productos.get(1).getNombre());
    }

    @Test
    void deberiaActualizarProductoValido() {
        Long id = 1L;
        Producto nuevoProducto = new Producto("Corrector", 2.0);
        Producto productoExistente = new Producto("Lapicero", 1.5);

        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id)).thenReturn(java.util.Optional.of(productoExistente));
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(nuevoProducto);

        ResponseEntity<?> response = productoController.actualizarProducto(id, nuevoProducto);

        assertEquals(200, response.getStatusCodeValue());
        Producto actualizado = (Producto) response.getBody();
        assertEquals("Corrector", actualizado.getNombre());
        assertEquals(2.0, actualizado.getPrecio());
    }

    @Test
    void deberiaRechazarActualizacionConNombreVacio() {
        Long id = 1L;
        Producto nuevoProducto = new Producto("   ", 2.0);

        ResponseEntity<?> response = productoController.actualizarProducto(id, nuevoProducto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El nombre no puede estar vacío.", response.getBody());
    }

    @Test
    void deberiaRechazarActualizacionConPrecioInvalido() {
        Long id = 1L;
        Producto nuevoProducto = new Producto("Corrector", -1.0);

        ResponseEntity<?> response = productoController.actualizarProducto(id, nuevoProducto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());
    }

    @Test
    void deberiaRetornar404AlActualizarProductoNoExistente() {
        Long id = 999L;
        Producto nuevoProducto = new Producto("Corrector", 2.0);

        when(productoRepository.existsById(id)).thenReturn(false);

        ResponseEntity<?> response = productoController.actualizarProducto(id, nuevoProducto);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Producto no encontrado.", response.getBody());
    }

    @Test
    void testSqlInjectionOnCreate() {
        // Simulate SQL injection attempt in product name
        Producto productoMalicioso = new Producto("'; DROP TABLE users; --", 10.0);

        // Since controller does not sanitize input, it will accept this string
        // We test that the controller accepts the input but in real app this should be sanitized
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(productoMalicioso);

        ResponseEntity<?> response = productoController.crearProducto(productoMalicioso);

        assertEquals(201, response.getStatusCodeValue());
        Producto guardado = (Producto) response.getBody();
        assertEquals("'; DROP TABLE users; --", guardado.getNombre());
    }

    @Test
    void testSqlInjectionOnUpdate() {
        Long id = 1L;
        Producto productoMalicioso = new Producto("'; DROP TABLE users; --", 10.0);
        Producto productoExistente = new Producto("Lapicero", 1.5);

        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id)).thenReturn(java.util.Optional.of(productoExistente));
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(productoMalicioso);

        ResponseEntity<?> response = productoController.actualizarProducto(id, productoMalicioso);

        assertEquals(200, response.getStatusCodeValue());
        Producto actualizado = (Producto) response.getBody();
        assertEquals("'; DROP TABLE users; --", actualizado.getNombre());
    }

    @Test
    void testConcurrentUpdateSameProduct() throws InterruptedException {
        Long id = 1L;
        Producto productoExistente = new Producto("Lapicero", 1.5);
        Producto productoUpdate1 = new Producto("Corrector", 2.0);
        Producto productoUpdate2 = new Producto("Cuaderno", 3.0);

        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id)).thenReturn(java.util.Optional.of(productoExistente));
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenReturn(productoUpdate1).thenReturn(productoUpdate2);

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        ResponseEntity<?>[] responses = new ResponseEntity<?>[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            final Producto updateProducto = (i == 0) ? productoUpdate1 : productoUpdate2;
            executor.submit(() -> {
                responses[index] = productoController.actualizarProducto(id, updateProducto);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        int successCount = 0;
        for (ResponseEntity<?> response : responses) {
            if (response.getStatusCodeValue() == 200) {
                successCount++;
            }
        }

        assertEquals(threadCount, successCount);
    }
}
