package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) ILIKE %:query%")
    Page<Customer> searchByName(@Param("query") String name, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) ILIKE %:query%")
    List<Customer> searchByName(@Param("query") String name);

}
