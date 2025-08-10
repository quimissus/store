package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.validate.RequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getAllCustomersPage(Pageable pageable) {
        final Page<Customer> customers = customerRepository.findAll(pageable);
        return customerMapper.customersToCustomerDTOs(customers.getContent());
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.customersToCustomerDTOs(customerRepository.findAll());
    }

    public List<CustomerDTO> getCustomerByName(String name, Pageable pageable) throws StoreIllegalArgument {
        RequestValidator.validateString(name);
        if (pageable == null) {
            return getCustomerByName(name);
        }
        final Page<Customer> customers = customerRepository.searchByName(name, pageable);
        return customerMapper.customersToCustomerDTOs(customers.getContent());
    }

    public List<CustomerDTO> getCustomerByName(String name) throws StoreIllegalArgument {
        RequestValidator.validateString(name);
        return customerMapper.customersToCustomerDTOs(customerRepository.searchByName(name));
    }

    public CustomerDTO createCustomer(Customer customer) {
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

}
