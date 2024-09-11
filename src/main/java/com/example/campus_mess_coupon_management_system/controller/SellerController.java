package com.example.campus_mess_coupon_management_system.controller;

import com.example.campus_mess_coupon_management_system.model.Coupon;
import com.example.campus_mess_coupon_management_system.security.CustomUserDetails;
import com.example.campus_mess_coupon_management_system.service.SellerService;
import com.example.campus_mess_coupon_management_system.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerSeller(@RequestBody Seller seller) {
        try {
            Seller registeredSeller = sellerService.registerSeller(seller);
            return ResponseEntity.ok(registeredSeller);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PatchMapping("updateSeller/{id}")
    public ResponseEntity<?> updateSellerDetails(@PathVariable Long id, @RequestBody Seller updatedSellerDetails) {
        // Get the authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the authenticated user has the ADMIN role
        if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            // If the user is an admin, allow updating any seller's details
            Seller updatedSeller = sellerService.updateSellerDetails(id, updatedSellerDetails);
            return ResponseEntity.ok(updatedSeller);
        }

        // If the user is not an admin, check if they are updating their own details
        if (!userDetails.getId().equals(id)) {
            // If the authenticated user is not the seller, return a forbidden response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this seller's details.");
        }

        // If the user is the seller, allow the update
        Seller updatedSeller = sellerService.updateSellerDetails(id, updatedSellerDetails);
        return ResponseEntity.ok(updatedSeller);
    }
}
