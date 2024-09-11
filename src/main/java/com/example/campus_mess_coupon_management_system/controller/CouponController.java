package com.example.campus_mess_coupon_management_system.controller;
import com.example.campus_mess_coupon_management_system.model.Coupon;
import com.example.campus_mess_coupon_management_system.security.CustomUserDetails;
import com.example.campus_mess_coupon_management_system.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);
    @Autowired
    private CouponService couponService;

    private boolean isAdmin(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @PostMapping("/addCoupon")
    public ResponseEntity<Coupon> addCoupon(@RequestBody Coupon coupon) {
        logger.info("Received request to add a new coupon: {}", coupon);
        Coupon savedCoupon = couponService.addCoupon(coupon);
        return ResponseEntity.ok(savedCoupon);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Coupon>> getAllActiveCoupons() {
        List<Coupon> coupons = couponService.getAllActiveCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PatchMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateCouponStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {

        // Get the authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the authenticated user has the ADMIN role
        if (isAdmin(userDetails))
        {
            // If the user is an admin, allow access to any seller's coupons
            Coupon updatedCoupon = couponService.updateCouponStatus(id, isActive);
            return ResponseEntity.ok(updatedCoupon);
        }
        // Check if the authenticated user's ID matches the seller ID
        if (!userDetails.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this coupon.");
        }
        Coupon updatedCoupon = couponService.updateCouponStatus(id, isActive);
        return ResponseEntity.ok(updatedCoupon);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getCouponsBySellerId(@PathVariable Long sellerId) {
        // Get the authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the authenticated user has the ADMIN role
        if (isAdmin(userDetails))
        {
            // If the user is an admin, allow access to any seller's coupons
            List<Coupon> coupons = couponService.getCouponsBySellerId(sellerId);
            return ResponseEntity.ok(coupons);
        }

        // Check if the authenticated user's ID matches the seller ID
        if (!userDetails.getId().equals(sellerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view these coupons.");
        }

        List<Coupon> coupons = couponService.getCouponsBySellerId(sellerId);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("getCoupon/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable Long id) {
        // Get the authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coupon coupon = couponService.getCouponById(id);
        // Check if the authenticated user has the ADMIN role
        if (isAdmin(userDetails))
        {
            // If the user is an admin, allow access to any seller's coupons
            return ResponseEntity.ok(coupon);
        }
        // Check if the authenticated user's ID matches the seller ID
        if (!userDetails.getId().equals(coupon.getSeller().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this coupon.");
        }
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("deleteCoupon/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        // Get the authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coupon coupon = couponService.getCouponById(id);
        // Check if the authenticated user has the ADMIN role
        if (isAdmin(userDetails))
        {
            // If the user is an admin, allow access to any seller's coupons
            couponService.deleteCoupon(id);
            return ResponseEntity.noContent().build();
        }
        // Check if the authenticated user's ID matches the seller ID
        if (!userDetails.getId().equals(coupon.getSeller().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this coupon.");
        }

        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("updateCoupon/{id}")
    public ResponseEntity<?> updateCouponDetails(@PathVariable Long id, @RequestBody Coupon updatedCouponDetails) {
        // Get the authenticated user details
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coupon coupon = couponService.getCouponById(id);
        // Check if the authenticated user has the ADMIN role
        if (isAdmin(userDetails))
        {
            // If the user is an admin, allow access to any seller's coupons
            Coupon updatedCoupon = couponService.updateCouponDetails(id, updatedCouponDetails);
            return ResponseEntity.ok(updatedCoupon);
        }
        // Check if the authenticated user's ID matches the seller ID
        if (!userDetails.getId().equals(coupon.getSeller().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this coupon.");
        }

        Coupon updatedCoupon = couponService.updateCouponDetails(id, updatedCouponDetails);
        return ResponseEntity.ok(updatedCoupon);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Coupon>> filterCoupons(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String mealSlot,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Coupon> filteredCoupons = couponService.filterCoupons(date, mealSlot, minPrice, maxPrice);
        return ResponseEntity.ok(filteredCoupons);
    }
}
