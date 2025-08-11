package com.example.store.controller;

import com.example.store.aspect.LogTime;
import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrdersDTO;
import com.example.store.entity.Product;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import com.example.store.service.AggregatorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductController {
    private final AggregatorService aggregatorService;

    public ProductController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody Product product) {
        return aggregatorService.createProduct(product);
    }

    @GetMapping("/{productId}")
    public ProductDTO findProductById(@PathVariable Long productId) throws StoreValueNotFound, StoreIllegalArgument {
        return aggregatorService.findProductById(productId);
    }

    @GetMapping("/order/{productId}")
    @LogTime
    public ProductOrdersDTO findOrdersById(@PathVariable Long productId) throws StoreValueNotFound, StoreIllegalArgument {
        return aggregatorService.findOrdersByProductId(productId);
    }

}
