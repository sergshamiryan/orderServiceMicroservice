package com.example.orderservice.dto;

import com.example.orderservice.model.OrderLineItems;

import java.util.List;

public record OrderRequest(List<OrderLineItemsRequest> orderLineItemsList) {
}
