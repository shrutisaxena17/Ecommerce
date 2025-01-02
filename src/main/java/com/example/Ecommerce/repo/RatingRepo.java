package com.example.Ecommerce.repo;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Rating;
import com.example.Ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RatingRepo extends JpaRepository<Rating,Long> {
    @Query("SELECT r FROM Rating r WHERE r.product.id = :productId AND r.user.id = :userId")
    Rating findByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);
}
