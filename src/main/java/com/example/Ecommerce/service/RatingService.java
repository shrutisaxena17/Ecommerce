package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.Rating;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repo.ProductRepo;
import com.example.Ecommerce.repo.RatingRepo;
import com.example.Ecommerce.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RatingRepo ratingRepo;

    public Rating addRating(Long userId, Long productId, int rating, String comment){
        Product product= productRepo.findById(productId).orElseThrow(()->new IllegalArgumentException("Product not found"));
        User user = userRepo.findById(userId).orElseThrow(()->new IllegalArgumentException("User id not found"));
        //check if the user has already rated the product
        Rating existingRating = ratingRepo.findByProductIdAndUserId(productId,userId);
        if(existingRating != null){
            throw new IllegalArgumentException("User has already rated this product");
        }
        Rating newRating = new Rating();
        newRating.setProduct(product);
        newRating.setUser(user);
        newRating.setRating(rating);
        newRating.setComment(comment);
        return ratingRepo.save(newRating);
    }

}
