package com.example.grocerynew.service;

import com.example.grocerynew.model.User;
import com.example.grocerynew.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieve the currently authenticated user
    public User getCurrentUser() {
        // Get the username of the authenticated user from Spring Security context
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authenticated user: " + currentUsername);
        return userRepository.findByUsername(currentUsername);
    }

    // Find a user by their username (used for other purposes like order creation)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Optionally, you can create a method to check if the user has a specific role.
    public boolean hasRole(String role) {
        UserDetails currentUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUserDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}