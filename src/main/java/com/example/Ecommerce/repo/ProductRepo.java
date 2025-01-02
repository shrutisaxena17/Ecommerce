package com.example.Ecommerce.repo;

import com.example.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findBySeller_Id(Long sellerId);

    @Query("SELECT p from Product p ORDER BY p.price ASC")
    List<Product> sortAllProductsByPrice();
}

