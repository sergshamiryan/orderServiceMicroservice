package com.example.orderservice.service;

import com.example.orderservice.dto.OrderLineItemsRequest;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.model.Orders;
import com.example.orderservice.repository.OrderRepository;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsList().stream()
                .map(this::mapToDto)
                .toList();

        Orders orders = Orders.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItems).build();

        orderRepository.save(orders);
    }

    private OrderLineItems mapToDto(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItems.builder()
                .price(orderLineItemsRequest.price())
                .quantity(orderLineItemsRequest.quantity())
                .skuCode(orderLineItemsRequest.skuCode()).build();
    }


}
