package com.example.campus_mess_coupon_management_system.service;

import com.example.campus_mess_coupon_management_system.model.Seller;
import com.example.campus_mess_coupon_management_system.repository.SellerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Slf4j
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Seller registerSeller(Seller seller) {
        // Check if a seller with the same email already exists
        if (sellerRepository.findByEmail(seller.getEmail()).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        // Hash the password before saving
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        return sellerRepository.save(seller);
    }
    public Seller updateSellerDetails(Long id, Seller updatedSellerDetails) {
        // Find the existing seller
        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));

        // Update fields only if they are provided
        if (updatedSellerDetails.getName() != null) {
            existingSeller.setName(updatedSellerDetails.getName());
        }

        if (updatedSellerDetails.getPassword() != null) {
            existingSeller.setPassword(passwordEncoder.encode(updatedSellerDetails.getPassword()));
        }

        if (updatedSellerDetails.getMobile() != null) {
            existingSeller.setMobile(updatedSellerDetails.getMobile());
        }

        return sellerRepository.save(existingSeller);
    }
}