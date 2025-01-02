package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repo.CartRepo;
import com.example.Ecommerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    CartRepo cartRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserService userService;

    public Cart getCartByUserId(Long userId) {
        return cartRepo.findByUser_Id(userId);
    }

    public Cart addProductToCart(Long userId, Long productId, int quantity) {

        User user =userService.getUserById(userId);

        Cart cart = getCartByUserId(userId);

        Optional<Product> productOptional = productRepo.findById(productId);

        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found for ID: " + productId);
        }

        Product product = productOptional.get();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setProducts(new ArrayList<>());
            cart.setQuantity(0);
        }

        List<Product> products = cart.getProducts();
        products.add(product);

        cart.setProducts(products);
        cart.setQuantity(cart.getQuantity() + quantity);

        return cartRepo.save(cart);
    }


    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);

        List<Product> products = cart.getProducts();
        products.removeIf(product -> product.getId().equals(productId));

        cart.setProducts(products);

        return cartRepo.save(cart);
    }

    public Cart removeProductsFromCart(Long userId, List<Long> productIds) {
        Cart cart = getCartByUserId(userId);
        List<Product> products = cart.getProducts();
        products.removeIf(product -> productIds.contains(product.getId()));
        cart.setProducts(products);
        return cartRepo.save(cart);
    }



    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getProducts().clear();
        cart.setQuantity(0);

        cartRepo.save(cart);
    }

}
