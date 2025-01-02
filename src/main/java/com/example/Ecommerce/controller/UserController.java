package com.example.Ecommerce.controller;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repo.UserRepo;
import com.example.Ecommerce.response.AuthenticationRequest;
import com.example.Ecommerce.response.UserIdResponse;
import com.example.Ecommerce.security.JwtService;
import com.example.Ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public ResponseEntity<UserIdResponse> loginUser(@RequestBody AuthenticationRequest request) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Optional<User> userOptional = userRepo.findByEmail(request.getEmail());
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getEmail()));

        String role= user.getRole().name();
        String jwt = jwtService.generateJwt(user.getEmail(),role);

        UserIdResponse response = new UserIdResponse("User logged in successfully!! Here is your JWT Token:",jwt);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String registrationId = userService.registerUser(user);
        return ResponseEntity.ok("OTP has been sent to the email provided. Use registration ID " + registrationId + " to verify.");
    }


    @PostMapping("/verifyOtp")
    public ResponseEntity<UserIdResponse> verifyOtp(@RequestParam String registrationId, @RequestParam String otp) {
        Long id = userService.verifyOtpAndCreateUser(registrationId, otp);
        if (id != null) {
            User user = userService.getUserById(id);
            String email= user.getEmail();
            String role = "ROLE_" + user.getRole().name();
            String jwt = jwtService.generateJwt(email,role);
            UserIdResponse response = new UserIdResponse("User registered successfully with User Id: "+id+" Here is your JWT Token:",jwt);
        return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
