package com.example.orderservice.dto;

import com.example.orderservice.model.OrderLineItems;

import java.math.BigDecimal;
import java.util.List;

public record OrderLineItemsRequest(Long id, String skuCode, BigDecimal price, Integer quantity) {
}
