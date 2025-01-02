package com.example.Ecommerce.repo;

import com.example.Ecommerce.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    Transaction findByStripePaymentIntentId(String paymentIntentId);
    Transaction findByOrderId(Long orderId);
}
