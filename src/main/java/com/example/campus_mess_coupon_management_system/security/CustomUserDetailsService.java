package com.example.campus_mess_coupon_management_system.security;

import com.example.campus_mess_coupon_management_system.model.Seller;
import com.example.campus_mess_coupon_management_system.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final SellerRepository sellerRepository;

    @Autowired
    public CustomUserDetailsService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Attempting to load user by email: {}", email);
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        logger.info("User found: {}", seller.getEmail());
        return new CustomUserDetails(seller);
    }
}
