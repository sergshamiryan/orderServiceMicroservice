package com.example.orderservice.dto;

import java.math.BigDecimal;

public record OrderLineItemsRequest(Long id, String skuCode, BigDecimal price, Integer quantity) {
}
