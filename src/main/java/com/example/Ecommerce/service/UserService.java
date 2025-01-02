package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Address;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    OtpService otpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(User user){
        String  registrationId= UUID.randomUUID().toString();
        String otp = otpService.generateOtp(registrationId);
        otpService.sendOtpByEmail(user.getEmail(),otp);
        otpService.storeUserData(registrationId,user);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return registrationId;
    }

    public Long verifyOtpAndCreateUser(String registrationId, String otp){
        if (otpService.isOtpValid(registrationId, otp)) {
            User user = otpService.getStoredUserData(registrationId);
            if (user != null) {
                if (user.getAddresses() != null) {
                    for (Address address : user.getAddresses()) {
                        address.setUser(user);
                    }
                }
                userRepo.save(user);
                return user.getId();
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    public User updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        userRepo.save(user);
        return user;
    }

    public void deleteUser(Long id) {
        try {
            userRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist.", e);
        }
    }

    public User getUserById(Long id){
           return userRepo.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public String getEmailByUserId(Long userId) {
        return userRepo.findById(userId)
                .map(User::getEmail)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    }

