package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsRequest;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.model.Orders;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsList().stream()
                .map(this::mapToDto)
                .toList();

        Orders orders = Orders.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItems).build();

        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse [] arr = webClient.get()
                .uri("http://localhost:8082/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();



        if (arr != null && Arrays.stream(arr).allMatch(InventoryResponse::isInStock)) {
            orderRepository.save(orders);
        } else {
            throw new IllegalArgumentException("Not in stock");
        }


    }

    private OrderLineItems mapToDto(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItems.builder()
                .price(orderLineItemsRequest.price())
                .quantity(orderLineItemsRequest.quantity())
                .skuCode(orderLineItemsRequest.skuCode()).build();
    }


}
