package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsRequest;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClient;
    private final KafkaTemplate<String,Map<String,String>> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = orderRequest.orderLineItemsList().stream()
                .map(this::mapToDto)
                .toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemList(orderLineItems).build();

        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItem::getSkuCode)
                .toList();


        InventoryResponse[] arr = webClient.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("sku-code", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        if (arr.length > 0 && Arrays.stream(arr).allMatch(InventoryResponse::isInStock)) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", Collections.singletonMap("orderNumber", order.getOrderNumber()));
        } else {
            throw new IllegalArgumentException("Not in stock");
        }


    }

    private OrderLineItem mapToDto(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItem.builder()
                .price(orderLineItemsRequest.price())
                .quantity(orderLineItemsRequest.quantity())
                .skuCode(orderLineItemsRequest.skuCode()).build();
    }


}
