package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.service.CustomerService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/page")
    public List<CustomerDTO> getAllCustomersPage(Pageable page) {
        return customerService.getAllCustomersPage(page);
    }

    @GetMapping("/{name}")
    public List<CustomerDTO> getCustomersByName(@PathVariable String name) throws StoreIllegalArgument {
        return customerService.getCustomerByName(name);
    }

    @GetMapping("/search")
    public List<CustomerDTO> getCustomersByName(@RequestParam String name, Pageable page) throws StoreIllegalArgument {
        return customerService.getCustomerByName(name, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }
}
