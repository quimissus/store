package com.example.store.repository;

import com.example.store.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //    @Query("SELECT new com.example.store.dto.ProductOrderDTO(p.id, p.name, o.id) " +
    //            "FROM Order o JOIN o.product p WHERE p.id = :productId")
    //    List<ProductOrderDTO> findProductOrderDTOByProductId(@Param("productId") Long productId);

}
