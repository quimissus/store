package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@WebMvcTest(CustomerServiceTest.class)
class CustomerServiceTest {

    private final Customer customer1 = new Customer(1L, "John Doe");
    private final Customer customer2 = new Customer(2L, "John Doe");
    private final Pageable pageable = PageRequest.of(0, 5);
    private final List<Customer> customers = List.of(customer1, customer2);
    private final Page<Customer> page = new PageImpl<>(customers, pageable, customers.size());
    private final Customer savedCustomer = new Customer();
    private final CustomerDTO customerDTO = new CustomerDTO();
    private final List<CustomerDTO> customerListDTO = List.of(new CustomerDTO(), new CustomerDTO());
    private final String name = "Jane";
    private final List<CustomerDTO> customerDTOList = List.of(new CustomerDTO());

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testGetAllCustomersPage_happyPath() {
        when(customerRepository.findAll(pageable)).thenReturn(page);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(customerListDTO);

        List<CustomerDTO> result = customerService.getAllCustomersPage(pageable);

        assertEquals(customerListDTO, result);
        verify(customerRepository).findAll(pageable);
        verify(customerMapper).customersToCustomerDTOs(customers);
    }

    @Test
    void testGetAllCustomers_happyPath() {

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(customerListDTO);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(customerListDTO, result);
        verify(customerRepository).findAll();
        verify(customerMapper).customersToCustomerDTOs(customers);
    }

    @Test
    void testGetCustomerByName_withPageable_happyPath() throws StoreIllegalArgument {
        Pageable pageableResult = PageRequest.of(0, 5);
        Page<Customer> pagination = new PageImpl<>(customers, pageableResult, customers.size());

        when(customerRepository.searchByName(name, pageableResult)).thenReturn(pagination);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(customerDTOList);

        List<CustomerDTO> result = customerService.getCustomerByName(name, pageableResult);

        assertEquals(customerDTOList, result);
        verify(customerRepository).searchByName(name, pageableResult);
        verify(customerMapper).customersToCustomerDTOs(customers);
    }

    @Test
    void testGetCustomerByName_callsNoPageableVersion() throws StoreIllegalArgument {

        CustomerService spyService = spy(customerService);
        doReturn(customerDTOList).when(spyService).getCustomerByName(name);

        List<CustomerDTO> result = spyService.getCustomerByName(name, null);

        assertEquals(customerDTO, result.get(0));
        verify(spyService).getCustomerByName(name);
    }

    @Test
    void testGetCustomerByName_withoutPageable_happyPath() throws StoreIllegalArgument {

        when(customerRepository.searchByName(name)).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(customerListDTO);

        List<CustomerDTO> result = customerService.getCustomerByName(name);

        assertEquals(customerListDTO, result);
        verify(customerRepository).searchByName(name);
        verify(customerMapper).customersToCustomerDTOs(customers);
    }

    @Test
    void testGetCustomerByName_not_happyPath() {

        StoreIllegalArgument result = assertThrows(StoreIllegalArgument.class, () -> {
            customerService.getCustomerByName("");
        });

        assertEquals("Provided String value is not valid", result.getMessage());
    }

    @Test
    void testCreateCustomer_happyPath() {
        when(customerRepository.save(customer1)).thenReturn(savedCustomer);
        when(customerMapper.customerToCustomerDTO(savedCustomer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(customer1);

        assertEquals(customerDTO, result);
        verify(customerRepository).save(customer1);
        verify(customerMapper).customerToCustomerDTO(savedCustomer);
    }
}
