package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found for ID: " + id));
    }

    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found for ID: " + id));
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());
        return productRepo.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
        } else {
            throw new RuntimeException("Product not found for ID: " + id);
        }
    }

    public List<Product> getProductsBySeller(Long id){
        return productRepo.findBySeller_Id(id);
    }

    public List<Product> sortProductsByPrice(){
     List<Product> productList=productRepo.sortAllProductsByPrice();
     return  productList;
    }

}
