package com.example.orderservice.repository;

import com.example.orderservice.model.Orders;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {



}
