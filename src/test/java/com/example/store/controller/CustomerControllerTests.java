package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
class CustomerControllerTests {

    private static final String URL = "/customer";
    private static final String SEARCH_URL = "/customer/search";
    private static final String PAGE_URL = "/customer/page";
    private final CustomerDTO customerDto = new CustomerDTO(1L, "John Doe");
    private final CustomerDTO expectedDTO = new CustomerDTO(1L, "John Doe");
    private final Customer customer = new Customer(1L, "John Doe");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void testCreateCustomer() throws Exception {
        Mockito.when(customerService.createCustomer(any(Customer.class))).thenReturn(customerDto);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andReturn();

        assertAll(() -> status().isCreated(), () -> jsonPath("$.name").value(expectedDTO.getName()));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(List.of(customerDto));

        mockMvc.perform(get(URL)).andReturn();
        assertAll(() -> status().isOk(), () -> jsonPath("$[0].name").value(expectedDTO.getName()));
    }

    @Test
    void testGetCustomerByName() throws Exception {
        Mockito.when(customerService.getCustomerByName(eq("John"), any(Pageable.class)))
                .thenReturn(List.of(customerDto));

        mockMvc.perform(get(SEARCH_URL).param("name", "John").param("page", "0").param("size", "10"))
                .andReturn();
        assertAll(() -> status().isOk(), () -> jsonPath("$[0].name").value(expectedDTO.getName()));
    }

    @Test
    void testGetCustomerByPage() throws Exception {

        Mockito.when(customerService.getAllCustomersPage(any(Pageable.class))).thenReturn(List.of(customerDto));

        mockMvc.perform(get(PAGE_URL).param("page", "0").param("size", "10")).andReturn();
        assertAll(() -> status().isOk(), () -> jsonPath("$[0].name").value(expectedDTO.getName()));
    }
}
