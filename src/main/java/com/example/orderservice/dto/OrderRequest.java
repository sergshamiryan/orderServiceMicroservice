package com.example.orderservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(@NotNull(message = "Cannot be null") List<OrderLineItemsRequest> orderLineItemsList) {

}
