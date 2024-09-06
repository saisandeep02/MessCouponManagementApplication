package com.example.campus_mess_coupon_management_system.controller;

import com.example.campus_mess_coupon_management_system.model.Coupon;
import com.example.campus_mess_coupon_management_system.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/addCoupon")
    public ResponseEntity<Coupon> addCoupon(@RequestBody Coupon coupon) {
        Coupon savedCoupon = couponService.addCoupon(coupon);
        return ResponseEntity.ok(savedCoupon);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Coupon>> getAllActiveCoupons() {
        List<Coupon> coupons = couponService.getAllActiveCoupons();
        return ResponseEntity.ok(coupons);
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Coupon> updateCouponStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        Coupon updatedCoupon = couponService.updateCouponStatus(id, isActive);
        return ResponseEntity.ok(updatedCoupon);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Coupon>> getCouponsBySellerId(@PathVariable Long sellerId) {
        List<Coupon> coupons = couponService.getCouponsBySellerId(sellerId);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("getCoupon/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("deleteCoupon/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("updateCoupon/{id}")
    public ResponseEntity<Coupon> updateCouponDetails(@PathVariable Long id, @RequestBody Coupon updatedCouponDetails) {
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
