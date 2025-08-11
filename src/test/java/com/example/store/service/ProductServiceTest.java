package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@WebMvcTest(ProductServiceTest.class)
class ProductServiceTest {

    private final Pageable pageable = PageRequest.of(0, 5);
    private final List<Product> products = List.of(new Product(), new Product());
    private final Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
    private final Long id = 1L;
    private final Product product = new Product();
    private final ProductDTO expectedProductDTO = new ProductDTO();
    private final List<ProductDTO> productDTOs = List.of(new ProductDTO(), new ProductDTO());
    private final Product savedProduct = new Product();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateProduct_happyPath() {

        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.productToProductDTO(savedProduct)).thenReturn(expectedProductDTO);

        ProductDTO result = productService.createProduct(product);

        assertEquals(expectedProductDTO, result);
        verify(productRepository).save(product);
        verify(productMapper).productToProductDTO(savedProduct);
    }

    @Test
    void testGetAllProductPage_happyPath() {

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.productsToProductsDTOs(products)).thenReturn(productDTOs);

        List<ProductDTO> result = productService.getAllProductPage(pageable);

        assertEquals(productDTOs, result);
        verify(productRepository).findAll(pageable);
        verify(productMapper).productsToProductsDTOs(products);
    }

    @Test
    void testFindProductById_happyPath() {

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO result = productService.findProductById(id);

        assertEquals(expectedProductDTO, result);
        verify(productRepository).findById(id);
        verify(productMapper).productToProductDTO(product);
    }

    @Test
    void testFindProductById_not_happyPath() {
        Long invalidId = -1L;

        StoreIllegalArgument exception = assertThrows(StoreIllegalArgument.class, () -> {
            productService.findProductById(invalidId);
        });

        assertEquals("illegal ID provided", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productMapper, never()).productToProductDTO(any());
    }

    @Test
    void testFindProductById_notFound() {
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        StoreValueNotFound exception = assertThrows(StoreValueNotFound.class, () -> {
            productService.findProductById(id);
        });

        assertEquals("product not found", exception.getMessage());
        verify(productRepository).findById(id);
        verify(productMapper, never()).productToProductDTO(any());
    }
}
