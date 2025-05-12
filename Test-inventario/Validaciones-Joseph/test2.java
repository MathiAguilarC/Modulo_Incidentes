package com.example.ProyectoInventarioF2;

import com.example.ProyectoInventarioF2.controlador.ProductoController;
import com.example.ProyectoInventarioF2.modelo.Producto;
import com.example.ProyectoInventarioF2.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoController
 * Incluye casos de prueba CP-01 a CP-05 con técnicas de
 * partición de equivalencia y valores límite
 */
public class test2 {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // CASOS DE PRUEBA PARA CREAR PRODUCTO (CP-01)

    @Test
    void CP01_01_deberiaCrearProductoValido() {
        // Arrange
        Producto producto = new Producto("Lapicero", 2.5);

        // Creamos un producto "guardado" que ya tendría su ID asignado automáticamente
        // por la base de datos. No podemos usar setId() ya que no existe en la clase Producto
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            // Simulamos la asignación de ID que haría JPA/Hibernate en la base de datos
            // Aquí usamos reflection para establecer el ID, ya que no hay un setter público
            try {
                java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(p, 1L);
                idField.setAccessible(false);
            } catch (Exception e) {
                // En caso de error, ignoramos y seguimos
            }
            return p;
        });

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Producto resultado = (Producto) response.getBody();
        assertNotNull(resultado);
        assertEquals("Lapicero", resultado.getNombre());
        assertEquals(2.5, resultado.getPrecio());
        assertEquals(1L, resultado.getId()); // Verificamos que tiene el ID que se asignó en el mock

        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_02_deberiaRechazarProductoConNombreNulo() {
        // Arrange
        Producto producto = new Producto(null, 10.0);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", response.getBody());

        verify(productoRepository, never()).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_03_deberiaRechazarProductoConNombreVacio() {
        // Arrange
        Producto producto = new Producto("", 10.0);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", response.getBody());

        verify(productoRepository, never()).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_04_deberiaRechazarProductoConNombreSoloEspacios() {
        // Arrange
        Producto producto = new Producto("   ", 10.0);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre es obligatorio y no puede estar vacío.", response.getBody());

        verify(productoRepository, never()).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_05_deberiaRechazarProductoConPrecioNulo() {
        // Arrange
        Producto producto = new Producto("Producto", null);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        verify(productoRepository, never()).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_06_deberiaRechazarProductoConPrecioCero() {
        // Arrange
        Producto producto = new Producto("Producto", 0.0);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        verify(productoRepository, never()).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_07_deberiaRechazarProductoConPrecioNegativo() {
        // Arrange
        Producto producto = new Producto("Producto", -1.0);

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        verify(productoRepository, never()).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP01_08_deberiaAceptarProductoConPrecioMinimo() {
        // Arrange - Valor límite: precio mínimo aceptable
        Producto producto = new Producto("Producto", 0.01);

        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            try {
                java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(p, 1L);
                idField.setAccessible(false);
            } catch (Exception e) {
                // Ignorar excepciones
            }
            return p;
        });

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    // CASOS DE PRUEBA PARA LISTAR PRODUCTOS (CP-02)

    @Test
    void CP02_01_deberiaListarTodosLosProductos() {
        // Arrange
        Producto producto1 = new Producto("Producto 1", 10.0);
        // Simulamos productos con ID ya asignado
        try {
            java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(producto1, 1L);
            idField.setAccessible(false);
        } catch (Exception e) {}

        Producto producto2 = new Producto("Producto 2", 20.0);
        // Simulamos productos con ID ya asignado
        try {
            java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(producto2, 2L);
            idField.setAccessible(false);
        } catch (Exception e) {}

        List<Producto> listaProductos = Arrays.asList(producto1, producto2);

        when(productoRepository.findAll()).thenReturn(listaProductos);

        // Act
        List<Producto> resultado = productoController.listarProductos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
        assertEquals("Producto 2", resultado.get(1).getNombre());

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void CP02_02_deberiaRetornarListaVaciaSiNoHayProductos() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Producto> resultado = productoController.listarProductos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(productoRepository, times(1)).findAll();
    }

    // CASOS DE PRUEBA PARA ACTUALIZAR PRODUCTO (CP-03)

    @Test
    void CP03_01_deberiaActualizarProductoExistente() {
        // Arrange
        Long id = 1L;
        Producto productoExistente = new Producto("Producto Original", 10.0);
        // Simulamos ID ya asignado
        try {
            java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(productoExistente, id);
            idField.setAccessible(false);
        } catch (Exception e) {}

        Producto productoActualizado = new Producto("Producto Actualizado", 15.0);

        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            return p; // Devolvemos el mismo producto actualizado
        });

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoActualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Producto resultado = (Producto) response.getBody();
        assertNotNull(resultado);
        assertEquals("Producto Actualizado", resultado.getNombre());
        assertEquals(15.0, resultado.getPrecio());
        assertEquals(id, resultado.getId());

        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP03_02_deberiaRechazarActualizacionConNombreInvalido() {
        // Arrange
        Long id = 1L;
        Producto productoActualizado = new Producto("", 15.0);

        // Act
        ResponseEntity<?> response =
                productoController.actualizarProducto(id, productoActualizado);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre no puede estar vacío.", response.getBody());

        // Al validar el nombre antes de existsById, no debe tocar el repositorio
        verifyNoInteractions(productoRepository);
    }


    @Test
    void CP03_03_deberiaRechazarActualizacionConPrecioInvalido() {
        // Arrange
        Long id = 1L;
        Producto productoActualizado = new Producto("Producto Actualizado", -5.0);

        // Act
        ResponseEntity<?> response =
                productoController.actualizarProducto(id, productoActualizado);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El precio debe ser mayor que cero.", response.getBody());

        // Como la validación ocurre antes de existsById, no debe llamar al repositorio
        verifyNoInteractions(productoRepository);
    }


    @Test
    void CP03_04_deberiaRechazarActualizacionDeProductoInexistente() {
        // Arrange
        Long id = 999L; // ID que no existe
        Producto productoActualizado = new Producto("Producto Actualizado", 15.0);

        when(productoRepository.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = productoController.actualizarProducto(id, productoActualizado);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado.", response.getBody());

        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, never()).findById(ArgumentMatchers.any());
        verify(productoRepository, never()).save(ArgumentMatchers.any());
    }

    // CASOS DE PRUEBA PARA ELIMINAR PRODUCTO (CP-04)

    @Test
    void CP04_01_deberiaEliminarProductoExistente() {
        // Arrange
        Long id = 1L;
        when(productoRepository.existsById(id)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(id);

        // Act
        ResponseEntity<?> response = productoController.eliminarProducto(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto eliminado exitosamente.", response.getBody());

        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void CP04_02_deberiaRechazarEliminarProductoInexistente() {
        // Arrange
        Long id = 999L; // ID que no existe
        when(productoRepository.existsById(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = productoController.eliminarProducto(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado.", response.getBody());

        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, never()).deleteById(ArgumentMatchers.any());
    }

    // CASOS DE PRUEBA ADICIONALES (CP-05)

    @Test
    void CP05_01_deberiaAceptarNombreLargo() {
        // Arrange - Probar un nombre muy largo pero válido
        String nombreLargo = "a".repeat(255); // 255 caracteres
        Producto producto = new Producto(nombreLargo, 10.0);

        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            try {
                java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(p, 1L);
                idField.setAccessible(false);
            } catch (Exception e) {}
            return p;
        });

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Producto resultado = (Producto) response.getBody();
        assertEquals(nombreLargo, resultado.getNombre());

        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }

    @Test
    void CP05_02_deberiaAceptarPrecioMuyAlto() {
        // Arrange - Probar un precio extremadamente alto pero válido
        Double precioAlto = Double.MAX_VALUE;
        Producto producto = new Producto("Producto Caro", precioAlto);

        when(productoRepository.save(ArgumentMatchers.any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            try {
                java.lang.reflect.Field idField = Producto.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(p, 1L);
                idField.setAccessible(false);
            } catch (Exception e) {}
            return p;
        });

        // Act
        ResponseEntity<?> response = productoController.crearProducto(producto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Producto resultado = (Producto) response.getBody();
        assertEquals(precioAlto, resultado.getPrecio());

        verify(productoRepository, times(1)).save(ArgumentMatchers.any(Producto.class));
    }
}