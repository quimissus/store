package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrdersDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exceptions.StoreIllegalArgument;
import com.example.store.exceptions.StoreValueNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AggregatorService {
    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;


    // Orders block //
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    public List<OrderDTO> getAllOrdersPage(Pageable pageable) {
        return orderService.getAllOrdersPage(pageable);
    }

    public OrderDTO findOrderById(final Long orderId) throws StoreIllegalArgument, StoreValueNotFound {
        return orderService.findOrderById(orderId);
    }

    public ProductOrdersDTO findOrderByProductId(final Long productId) throws StoreIllegalArgument, StoreValueNotFound {
        ProductDTO product = productService.findProductById(productId);
        List<Order> orderList = orderService.findOrdersByProductId(productId);

        ProductOrdersDTO productOrdersDTO = new ProductOrdersDTO();
        productOrdersDTO.setId(productId);
        List<Long> orderId = orderList.stream().map(Order::getId).toList();
        productOrdersDTO.setOrderId(orderId);
        productOrdersDTO.setDescription(product.getDescription());
        return productOrdersDTO;
    }

    public OrderDTO createOrder(final Order order) throws StoreValueNotFound, StoreIllegalArgument {
            for(Product product: order.getProduct()){
                // fix this block.
                if (productService.findProductById(product.getId()) == null){
                    throw new StoreValueNotFound("Product not found");
                }
            }

        return orderService.createOrder(order);
    }

    // product block //
    public ProductDTO findProductById(@PathVariable Long productId) throws StoreIllegalArgument, StoreValueNotFound {
        return  productService.findProductById(productId);
    }

    public ProductOrdersDTO findOrdersByProductId(@PathVariable Long productId) throws StoreIllegalArgument, StoreValueNotFound {
        return findOrderByProductId(productId);
    }

    public ProductDTO createProduct(Product product) {
        return productService.createProduct(product);
    }
}


