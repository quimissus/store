package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.AggregatorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AggregatorService aggregatorService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/order";

    private final ProductDTO productDTO = new ProductDTO(1L, "Product A");
    private final CustomerDTO customerDTO = new CustomerDTO(1L, "John Doe");
    private final OrderDTO orderDTO = new OrderDTO(
            1L,
            "Sample Order",
            customerDTO,
            List.of(productDTO)
    );


    @Test
    void testCreateOrder() throws Exception {
        Mockito.when(aggregatorService.createOrder(any(Order.class)))
                .thenReturn(orderDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO))).andReturn();

        assertAll(() -> status().isCreated(),
                () -> jsonPath("$.description").value("Sample Order"),
                () -> jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testGetOrder() throws Exception {
        Mockito.when(aggregatorService.getAllOrders())
                .thenReturn(List.of(orderDTO));

        mockMvc.perform(get(URL)).andReturn();

        assertAll("expectations",
                () -> status().isOk(),
                () -> jsonPath("$[0].id").value(1L),
                () -> jsonPath("$[0].description").value("Sample Order"),
                () -> jsonPath("$[0].customer.name").value("John Doe")
        );
    }

    @Test
    void testFindOrderById() throws Exception {
        Mockito.when(aggregatorService.findOrderById(1L))
                .thenReturn(orderDTO);
        mockMvc.perform(get(URL + "/1")).andReturn();
        assertAll("expectations",
                () -> status().isOk(),
                () -> jsonPath("$.id").value(1L),
                () -> jsonPath("$.customer.name").value("John Doe"),
                () -> jsonPath("$.product[0].name").value("Product A")
        );
    }
}
