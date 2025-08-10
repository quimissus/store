package com.example.store.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductOrdersDTO {
    private Long id;
    private String description;
    private List<Long> orderId;
}
