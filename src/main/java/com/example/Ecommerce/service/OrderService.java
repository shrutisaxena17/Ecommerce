package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.*;
import com.example.Ecommerce.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    TransactionService transactionService;

    public Order createOrder(Long userId, List<Long> productIds, List<Integer> quantities) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepo.findByUser_Id(userId);

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(Order.Status.PENDING);


        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productRepo.findById(productIds.get(i)).orElseThrow(() -> new RuntimeException("Product not found"));
            int quantity = quantities.get(i);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            total = total.add(orderItem.getPrice());
            orderItems.add(orderItem);
        }

        order.setTotal(total);
        order.setOrderItems(orderItems);
        return order;
    }

    public void saveOrder(Order order) {
        Order savedOrder = orderRepo.save(order);

        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(savedOrder);
            orderItemRepo.save(orderItem);
        }
    }

    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }


    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepo.findByUser_Id(userId);
    }

    public Order   updateOrderStatus(Long orderId, Order.Status status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
//        Message.creator(new PhoneNumber("+91 95608 91243"),
//                new PhoneNumber("+17752589589"), "Hello from your favorite Ecommerce Shopping Place! \n Your Order Status has changed. It is now "+order.getStatus()+"\n Thankyou for shopping!").create();
        return orderRepo.save(order);

    }

    public void cancelOrder(Long orderId) {
        transactionService.cancelTransaction(orderId);
        updateOrderStatus(orderId, Order.Status.CANCELLED);
    }
}

