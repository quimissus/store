package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrdersDTO;
import com.example.store.entity.Product;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import com.example.store.service.AggregatorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductControllerTest.class)
class ProductControllerTest {

    private static final String URL = "/product";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductDTO expectedProductDTO = new ProductDTO(1L, "Test Product");
    private final ProductOrdersDTO ordersDTO = new ProductOrdersDTO(1L, "product dto", List.of(1L, 2L));
    private final Product inputProduct = new Product(1L, "Test Product");

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AggregatorService aggregatorService;

    @InjectMocks
    private ProductController productController;

    @Test
    void testCreateProduct_happyPath() throws Exception {
        when(aggregatorService.createProduct(inputProduct)).thenReturn(expectedProductDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputProduct)))
                .andReturn();
        assertAll(() -> status().isCreated(), () -> jsonPath("$.id").value(1), () -> jsonPath("$.description")
                .value("Test Product"));
    }

    @Test
    void testFindProductById_happyPath() throws Exception {
        Long productId = 1L;
        when(aggregatorService.findProductById(productId)).thenReturn(expectedProductDTO);

        mockMvc.perform(get(URL + "/" + productId)).andReturn();
        assertAll(() -> status().isOk(), () -> jsonPath("$.id").value(productId), () -> jsonPath("$.description")
                .value("Test Product"));
    }

    @Test
    void testFindOrdersById_happyPath() throws Exception {
        Long productId = 1L;

        when(aggregatorService.findOrdersByProductId(productId)).thenReturn(ordersDTO);

        mockMvc.perform(get(URL + "/order/" + productId)).andReturn();

        assertAll(() -> status().isOk(), () -> jsonPath("$.productId").value(productId), () -> jsonPath(
                        "$.orders.length()")
                .value(2));
    }

    @Test
    void testFindProductById_storeValueNotFound() throws Exception {
        when(aggregatorService.findProductById(999L)).thenThrow(new StoreValueNotFound("not found"));

        mockMvc.perform(get(URL + "/999")).andExpect(status().isInternalServerError());
    }

    @Test
    void testFindProductById_illegalArgument() throws Exception {
        when(aggregatorService.findProductById(-1L)).thenThrow(new StoreIllegalArgument("invalid"));

        mockMvc.perform(get(URL + "/-1")).andExpect(status().isInternalServerError());
    }
}
