package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.validate.RequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDTO> getAllOrders() {
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }

    public List<OrderDTO> getAllOrdersPage(Pageable pageable) {
            final Page<Order> orders = orderRepository.findAll( pageable);
            return orderMapper.ordersToOrderDTOs(orders.getContent());
    }

    public OrderDTO findOrderById(final Long orderId) throws StoreIllegalArgument, StoreValueNotFound {
        RequestValidator.validateId(orderId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(orderMapper::orderToOrderDTO).orElseThrow(() ->  new StoreValueNotFound("Order ID not fund"));
    }


    public List<Order> findOrdersByProductId(final Long productId) throws StoreIllegalArgument, StoreValueNotFound {
        RequestValidator.validateId(productId);
        List<Order> optionalOrder = orderRepository.findByProduct_Id(productId);
        System.out.print(" orderlist " + optionalOrder);
        //  [Order(id=10014, description=Handcrafted Soft Chair 31, customer=Customer(id=6, name=Vicki Kutch), product=[Product(id=3, description=order1test 3)]),
      //  Order(id=10015, description=Handcrafted Soft Chair 31, customer=Customer(id=6, name=Vicki Kutch), product=[Product(id=3, description=order1test 3)]),
      //  Order(id=10016, description=Handcrafted Soft Chair 31, customer=Customer(id=6, name=Vicki Kutch), product=[Product(id=3, description=order1test 3)])]
        return optionalOrder;
    }
    public OrderDTO createOrder(final Order order) {
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }

}
