package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;

import com.example.store.service.AggregatorService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final AggregatorService aggregatorService;


    public OrderController(AggregatorService aggregatorService){
        this.aggregatorService = aggregatorService;
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return aggregatorService.getAllOrders();
    }

    @GetMapping("/page")
    public List<OrderDTO> getAllOrdersPage(Pageable pageable) {
        return aggregatorService.getAllOrdersPage(pageable);
    }

    @GetMapping("/{orderId}")
    public OrderDTO findOrderById(@PathVariable Long orderId) throws StoreIllegalArgument, StoreValueNotFound {
        return aggregatorService.findOrderById(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody Order order) throws StoreValueNotFound, StoreIllegalArgument {
        return aggregatorService.createOrder(order);
    }
}
