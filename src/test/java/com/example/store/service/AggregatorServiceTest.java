package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrdersDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exceptions.StoreValueNotFound;

import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(AggregatorServiceTest.class)
class AggregatorServiceTest {

    private final Long orderId = 1L;
    private final Long productId = 1L;
    private final ProductDTO productDTO = new ProductDTO();
    private final Order order = new Order();
    private final Order order2 = new Order();
    private final List<Order> orderList = List.of(order, order2);
    private final OrderDTO expectedOrderDTO = new OrderDTO();
    private final OrderDTO orderDTO = new OrderDTO();
    private final List<OrderDTO> orders = List.of(orderDTO);
    private final List<ProductDTO> productDTOList = List.of(productDTO);
    private final Product product = new Product();
    private final List<Product> productList = List.of(product);

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AggregatorService aggregatorService;

    @Test
    void testGetAllOrders_returnsList() {
        when(orderService.getAllOrders()).thenReturn(orders);

        List<OrderDTO> result = aggregatorService.getAllOrders();

        assertEquals(orders, result);
        verify(orderService).getAllOrders();
    }

    @Test
    void testGetAllOrdersPage_returnsPagedList() {
        Pageable pageable = mock(Pageable.class);
        when(orderService.getAllOrdersPage(pageable)).thenReturn(orders);

        List<OrderDTO> result = aggregatorService.getAllOrdersPage(pageable);

        assertEquals(orders, result);
        verify(orderService).getAllOrdersPage(pageable);
    }

    @Test
    void testFindOrderById_validId_returnsDTO() {

        when(orderService.findOrderById(orderId)).thenReturn(orderDTO);

        OrderDTO result = aggregatorService.findOrderById(orderId);

        assertEquals(expectedOrderDTO, result);
        verify(orderService).findOrderById(orderId);
    }

    @Test
    void testFindOrderByProductId_happyPath() {
        order.setId(100L);
        order2.setId(200L);

        when(productService.findProductById(productId)).thenReturn(productDTO);
        when(orderService.findOrdersByProductId(productId)).thenReturn(orderList);

        ProductOrdersDTO result = aggregatorService.findOrderByProductId(productId);

        assertAll(
                () -> assertEquals(productId, result.getId()),
                () -> assertEquals(productDTO.getDescription(), result.getDescription()),
                () -> assertEquals(List.of(100L, 200L), result.getOrderId()));

        verify(productService).findProductById(productId);
        verify(orderService).findOrdersByProductId(productId);
    }

    @Test
    void testCreateOrder_allProductsExist_createsOrder() {
        order.setProduct(productList);
        expectedOrderDTO.setProduct(productDTOList);
        when(productService.findProductById(any())).thenReturn(productDTO);
        when(orderService.createOrder(order)).thenReturn(expectedOrderDTO);

        OrderDTO result = aggregatorService.createOrder(order);

        assertEquals(expectedOrderDTO, result);
        verify(productService).findProductById(any());
        verify(orderService).createOrder(order);
    }

    @Test
    void testCreateOrder_missingProduct() {
        product.setId(productId);
        order.setProduct(productList);

        when(productService.findProductById(any())).thenThrow(new StoreValueNotFound("Product not found"));

        StoreValueNotFound result = assertThrows(StoreValueNotFound.class, () -> {
            aggregatorService.createOrder(order);
        });

        assertEquals("Product not found", result.getMessage());
        verify(productService).findProductById(anyLong());
        verify(orderService, never()).createOrder(any());
    }

    @Test
    void testFindProductById_happyPath() {
        when(productService.findProductById(productId)).thenReturn(productDTO);

        ProductDTO result = aggregatorService.findProductById(productId);

        assertEquals(productDTO, result);
        verify(productService).findProductById(productId);
    }

    @Test
    @SneakyThrows
    void testFindOrdersByProductId_happyPath() {

        when(productService.findProductById(productId)).thenReturn(new ProductDTO());
        when(orderService.findOrdersByProductId(productId)).thenReturn(List.of());

        ProductOrdersDTO result = aggregatorService.findOrdersByProductId(productId);

        assertNotNull(result);
        verify(productService).findProductById(productId);
        verify(orderService).findOrdersByProductId(productId);
    }

    @Test
    void testCreateProduct_happyPath() {
        when(productService.createProduct(product)).thenReturn(productDTO);

        ProductDTO result = aggregatorService.createProduct(product);

        assertEquals(productDTO, result);
        verify(productService).createProduct(product);
    }
}
