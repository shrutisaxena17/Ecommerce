package com.example.Ecommerce.service;
import com.example.Ecommerce.entity.Order;
import com.example.Ecommerce.entity.Transaction;
import com.example.Ecommerce.repo.TransactionRepo;
import com.example.Ecommerce.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class TransactionService {

        private final TransactionRepo transactionRepository;

        public TransactionService(TransactionRepo transactionRepository) {
            this.transactionRepository = transactionRepository;
        }

        @Autowired
        UserRepo userRepo;

        public Transaction createTransaction(Order order, String paymentIntentId, String customerId, BigDecimal amount) {

            Transaction transaction = new Transaction();
            transaction.setOrder(order);
            transaction.setAmount(amount);
            transaction.setPaymentMethod("Stripe");
            transaction.setStatus(Transaction.Status.COMPLETED);
            transaction.setStripePaymentIntentId(paymentIntentId);
            transaction.setStripeCustomerId(customerId);

            return transactionRepository.save(transaction);
        }

    public void updateTransactionStatus(String paymentIntentId, Transaction.Status status) {

            Transaction transaction = transactionRepository.findByStripePaymentIntentId(paymentIntentId);
            if (transaction != null) {
                transaction.setStatus(status);
                transactionRepository.save(transaction);
            }
        }

    public void cancelTransaction(Long orderId) {
    Transaction transaction = transactionRepository.findByOrderId(orderId);
    transaction.setStatus(Transaction.Status.RETURNED);
    }
}

