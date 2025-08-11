package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;

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

@WebMvcTest(OrderServiceTest.class)
class OrderServiceTest {

    private final List<Order> orders = List.of(new Order(), new Order());
    private final List<Order> expectedOrders = List.of(new Order(), new Order());

    private final List<OrderDTO> dtos = List.of(new OrderDTO(), new OrderDTO());
    private final Pageable pageable = PageRequest.of(0, 5);
    private final Page<Order> orderPage = new PageImpl<>(orders);
    private final Long orderId = 1L;
    private final Order order = new Order();
    private final OrderDTO orderDTO = new OrderDTO();
    private final Long productId = 10L;
    private final Order savedOrder = new Order();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetAllOrders_happyPath() {
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.ordersToOrderDTOs(orders)).thenReturn(dtos);

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(dtos, result);
        verify(orderRepository).findAll();
        verify(orderMapper).ordersToOrderDTOs(orders);
    }

    @Test
    void testGetAllOrdersPage_happyPath() {

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.ordersToOrderDTOs(orders)).thenReturn(dtos);

        List<OrderDTO> result = orderService.getAllOrdersPage(pageable);

        assertEquals(dtos, result);
        verify(orderRepository).findAll(pageable);
        verify(orderMapper).ordersToOrderDTOs(orders);
    }

    @Test
    void testFindOrderById_happyPath() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.findOrderById(orderId);

        assertEquals(orderDTO, result);
        verify(orderRepository).findById(orderId);
        verify(orderMapper).orderToOrderDTO(order);
    }

    @Test
    void testFindOrderById_illegalArgument() {
        Long invalidId = -5L;

        StoreIllegalArgument result =
                assertThrows(StoreIllegalArgument.class, () -> orderService.findOrderById(invalidId));

        assertEquals("illegal ID provided", result.getMessage());
        verify(orderRepository, never()).findById(any());
        verify(orderMapper, never()).orderToOrderDTO(any());
    }

    @Test
    void testFindOrderById_valueNotFound() {
        Long id = 100L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        StoreValueNotFound result = assertThrows(StoreValueNotFound.class, () -> orderService.findOrderById(id));

        assertEquals("Order ID not fund", result.getMessage()); // Keep the typo if it's in your real code
        verify(orderRepository).findById(id);
        verify(orderMapper, never()).orderToOrderDTO(any());
    }

    @Test
    void testFindOrdersByProductId_happyPath() throws StoreIllegalArgument, StoreValueNotFound {
        when(orderRepository.findByProduct_Id(productId)).thenReturn(orders);

        List<Order> result = orderService.findOrdersByProductId(productId);

        assertEquals(expectedOrders, result);
        verify(orderRepository).findByProduct_Id(productId);
    }

    @Test
    void testFindOrdersByProductId_illegalArgument() {
        Long invalidProductId = -1L;

        StoreIllegalArgument result =
                assertThrows(StoreIllegalArgument.class, () -> orderService.findOrdersByProductId(invalidProductId));

        assertEquals("illegal ID provided", result.getMessage());
        verify(orderRepository, never()).findByProduct_Id(any());
    }

    @Test
    void testCreateOrder_happyPath() {
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.orderToOrderDTO(savedOrder)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(order);

        assertEquals(orderDTO, result);
        verify(orderRepository).save(order);
        verify(orderMapper).orderToOrderDTO(savedOrder);
    }
}
