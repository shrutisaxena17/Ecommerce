package com.example.Ecommerce.controller;

import com.example.Ecommerce.entity.Order;
import com.example.Ecommerce.payment.StripeResponse;
import com.example.Ecommerce.payment.StripeService;
import com.example.Ecommerce.service.CartService;
import com.example.Ecommerce.service.OrderService;
import com.example.Ecommerce.service.TransactionService;
import com.example.Ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @Autowired
    StripeService stripeService;

    @Autowired
    TransactionService transactionService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StripeResponse> createOrder(
            @RequestParam Long userId,
            @RequestParam List<Long> productIds,
            @RequestParam List<Integer> quantities) {

        Order order = orderService.createOrder(userId, productIds, quantities);
        cartService.removeProductsFromCart(userId, productIds);
        String customerEmail = userService.getEmailByUserId(userId);
        StripeResponse stripeResponse = stripeService.checkoutProducts(order, customerEmail);
        if (stripeResponse.getStatus().equals("SUCCESS")) {
            order.setStatus(Order.Status.PENDING);
            orderService.saveOrder(order);
            transactionService.createTransaction(order,stripeResponse.getSessionId(), String.valueOf(userId),order.getTotal());
        }
        return ResponseEntity.ok(stripeResponse);
    }


    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam Order.Status status) {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);

    }

    @GetMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Your Order is cancelled! The Order Amount will be returned to your account soon");
    }
}