package com.example.Ecommerce.repo;

import com.example.Ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart,Long> {
   Cart findByUser_Id(Long userId);
}
