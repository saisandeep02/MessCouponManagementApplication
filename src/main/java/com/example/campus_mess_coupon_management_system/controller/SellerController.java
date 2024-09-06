package com.example.campus_mess_coupon_management_system.controller;

import com.example.campus_mess_coupon_management_system.service.SellerService;
import com.example.campus_mess_coupon_management_system.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PutMapping("updateSeller/{id}")
    public ResponseEntity<Seller> updateSellerDetails(@PathVariable Long id, @RequestBody Seller updatedSellerDetails) {
        Seller updatedSeller = sellerService.updateSellerDetails(id, updatedSellerDetails);
        return ResponseEntity.ok(updatedSeller);
    }
}
