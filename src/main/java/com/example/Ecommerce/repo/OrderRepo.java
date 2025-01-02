package com.example.Ecommerce.repo;

import com.example.Ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByUser_Id(Long userId);
}
