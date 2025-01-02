package com.example.Ecommerce.controller;

import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{id}/add")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long id,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            Cart updatedCart = cartService.addProductToCart(id, productId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Long userId,
            @RequestParam Long productId) {
        try {
            Cart updatedCart = cartService.removeProductFromCart(userId, productId);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
