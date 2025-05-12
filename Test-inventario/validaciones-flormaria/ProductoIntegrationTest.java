package com.example.ProyectoInventarioF2.integration;

import com.example.ProyectoInventarioF2.model.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // CP-06: Actualizar producto existente
    @Test
    public void testActualizarProducto_Existente() throws Exception {
        Producto producto = new Producto(1L, "NuevoNombre", 5.0);
        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("NuevoNombre"))
                .andExpect(jsonPath("$.precio").value(5.0));
    }

    // CP-07: Actualizar producto inexistente
    @Test
    public void testActualizarProducto_NoExistente() throws Exception {
        Producto producto = new Producto(99L, "Corrector", 1.5);
        mockMvc.perform(put("/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    // CP-08: Eliminar producto existente
    @Test
    public void testEliminarProducto_Existente() throws Exception {
        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto eliminado exitosamente")));
    }

    // CP-09: Eliminar producto inexistente
    @Test
    public void testEliminarProducto_NoExistente() throws Exception {
        mockMvc.perform(delete("/productos/100"))
                .andExpect(status().isNotFound());
    }

    // CP-10: Actualizar producto con nombre vacío
    @Test
    public void testActualizarProducto_NombreVacio() throws Exception {
        Producto producto = new Producto(1L, "", 5.0);
        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest()); // Esperado: 400 Bad Request
    }

    // CP-11: Actualizar producto con precio infinito
    @Test
    public void testActualizarProducto_PrecioInfinito() throws Exception {
        String jsonInvalido = """
            {
              "id": 1,
              "nombre": "Lapicero",
              "precio": Infinity
            }
            """;
        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest()); // Esperado: 400 Bad Request
    }

    // CP-12: Crear producto con datos nulos
    @Test
    public void testCrearProducto_DatosNulos() throws Exception {
        Producto producto = new Producto(null, null, -1.0); // id nulo, nombre nulo, precio negativo
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest()); // Debería retornar un 400 por datos inválidos
    }

    // CP-13: Crear producto con precio negativo
    @Test
    public void testCrearProducto_PrecioNegativo() throws Exception {
        Producto producto = new Producto(null, "ProductoNegativo", -5.0);
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest()); // Esperado: 400 Bad Request, no debería aceptar precios negativos
    }

    // CP-14: Crear producto con nombre vacío
    @Test
    public void testCrearProducto_NombreVacio() throws Exception {
        Producto producto = new Producto(null, "", 5.0);
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest()); // Debería devolver un 400 por nombre vacío
    }
}


