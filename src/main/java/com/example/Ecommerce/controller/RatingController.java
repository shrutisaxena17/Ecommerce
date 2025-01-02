package com.example.Ecommerce.controller;
import com.example.Ecommerce.entity.Rating;
import com.example.Ecommerce.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/add")
    public ResponseEntity<Rating> AddRating(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int rating, @RequestParam(required = false) String comment){
            Rating productRating = ratingService.addRating(userId, productId, rating, comment);
            return ResponseEntity.ok(productRating);
    }

}
