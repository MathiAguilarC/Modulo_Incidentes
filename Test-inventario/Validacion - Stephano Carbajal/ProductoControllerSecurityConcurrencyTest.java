package com.example.ProyectoInventarioF2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;

public class ProductoControllerSecurityConcurrencyTest {

    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoController = new ProductoController(productoRepository);
    }

    @Test
    void testSqlInjectionInDeleteId() {
        // Simulate SQL injection attempt by passing malicious id string
        // Since id is Long, this will cause a 400 Bad Request at Spring level
        // We simulate by calling the method with null or invalid id and expect error handling

        // The controller expects Long id, so invalid string won't reach here in real app
        // We test by simulating a call with null or invalid id scenario

        // Here we test that the controller rejects null id or non-existing id gracefully
        Long maliciousId = null; // simulate invalid id

        ResponseEntity<?> response = productoController.eliminarProducto(maliciousId);
        // Since null id will cause NullPointerException, we expect to handle it gracefully in real app
        // But here we check if existsById is called with null and returns false
        verify(productoRepository, times(1)).existsById(maliciousId);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testConcurrentDeleteSameId() throws InterruptedException {
        Long productId = 5L;

        // Setup mock behavior
        when(productoRepository.existsById(productId)).thenReturn(true).thenReturn(false);
        doNothing().when(productoRepository).deleteById(productId);

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        ResponseEntity<?>[] responses = new ResponseEntity<?>[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                responses[index] = productoController.eliminarProducto(productId);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        // One should succeed (200), one should fail (404)
        int successCount = 0;
        int notFoundCount = 0;
        for (ResponseEntity<?> response : responses) {
            if (response.getStatusCodeValue() == 200) {
                successCount++;
            } else if (response.getStatusCodeValue() == 404) {
                notFoundCount++;
            }
        }

        assertEquals(1, successCount);
        assertEquals(1, notFoundCount);
    }
}
