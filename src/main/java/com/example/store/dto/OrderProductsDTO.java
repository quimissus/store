package com.example.store.dto;

import com.example.store.entity.Order;
import com.example.store.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class OrderProductsDTO {
    private Order order;
    private List<Product> product;
}
