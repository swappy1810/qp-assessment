package com.example.grocerynew.config;

import com.example.grocerynew.enums.RoleName;
import com.example.grocerynew.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    // Constructor-based injection of UserRepository
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Configures HTTP security (requests filtering).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF for simplicity (ensure this is secure in production)
                .authorizeHttpRequests() // Replaced `authorizeRequests()` with `authorizeHttpRequests()`
                .requestMatchers("/grocery-items").hasAuthority("ROLE_ADMIN")  // Only admin can access /grocery-items
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")      // Admin role required for /admin/**
                .requestMatchers("/user").hasAuthority("ROLE_USER")         // User role required for /orders
                .anyRequest().authenticated() // Secure other requests
                .and()
                .formLogin().permitAll() // Allow form-based login
                .and()
                .httpBasic(); // Enable HTTP Basic authentication (useful for testing)

        return http.build(); // Return the configured SecurityFilterChain
    }

    /**
     * Configures the AuthenticationManager bean for custom authentication.
     * Removed explicit passwordEncoder() and AuthenticationManager() methods here.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService()) // Custom UserDetailsService
                .passwordEncoder(passwordEncoder()) // Password encoder
                .and().build();
    }

    /**
     * Configures the UserDetailsService that loads user data from the repository.
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            var user = userRepository.findByUsername(username);
//            if (user == null) {
//                throw new RuntimeException("User not found!");
//            }
//            var authorities = user.getRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name())) // Adjust according to your Role enum
//                    .collect(Collectors.toList());
//
//
//            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
//        };
//    }

    // Define the UserDetailsService with in-memory users
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin@123")) // {noop} means no encoding for the password (used for testing only)
                        .roles("ADMIN")
                        .build(),
                User.withUsername("user")
                        .password(passwordEncoder().encode("user@123"))
                        .roles("USER")
                        .build()
        );
    }

    /**
     * Provides the PasswordEncoder to securely encode and check passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Using BCrypt for secure password hashing
    }

    /**
     * Provides a DaoAuthenticationProvider to manage authentication.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService()); // Set custom UserDetailsService
        provider.setPasswordEncoder(passwordEncoder()); // Set password encoder
        return provider;
    }

}
