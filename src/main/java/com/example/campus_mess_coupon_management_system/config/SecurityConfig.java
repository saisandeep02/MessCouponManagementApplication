package com.example.campus_mess_coupon_management_system.config;

import com.example.campus_mess_coupon_management_system.repository.SellerRepository;
import com.example.campus_mess_coupon_management_system.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/sellers/register", "/api/login").permitAll()  // Allow these without authentication
                        .requestMatchers("/api/coupons/**").authenticated()  // Secure coupon-related endpoints
                        .anyRequest().authenticated()  // Secure all other endpoints
                )
                .httpBasic(withDefaults());  // Enable HTTP Basic authentication

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider(passwordEncoder, userDetailsService))
                .build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService(SellerRepository sellerRepository) {
        return new CustomUserDetailsService(sellerRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
