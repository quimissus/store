package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import com.example.store.validate.RequestValidator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDTO createProduct(Product product) {
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    public List<ProductDTO> getAllProductPage(Pageable pageable) {
        final Page<Product> products = productRepository.findAll(pageable);
        return productMapper.productsToProductsDTOs(products.getContent());
    }

    public ProductDTO findProductById(Long productId) throws StoreIllegalArgument, StoreValueNotFound {
        RequestValidator.validateId(productId);
        Optional<Product> optionalOrder = productRepository.findById(productId);
        // Error messages should be from enum.
        return optionalOrder
                .map(productMapper::productToProductDTO)
                .orElseThrow(() -> new StoreValueNotFound("product not found"));
    }
}
