package com.example.store.repository;

import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query("SELECT new com.example.store.dto.ProductOrderDTO(p.id, p.name, o.id) " +
//            "FROM Order o JOIN o.product p WHERE p.id = :productId")
//    List<ProductOrderDTO> findProductOrderDTOByProductId(@Param("productId") Long productId);


}
