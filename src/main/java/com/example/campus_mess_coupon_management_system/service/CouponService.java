package com.example.campus_mess_coupon_management_system.service;

import com.example.campus_mess_coupon_management_system.model.Coupon;
import com.example.campus_mess_coupon_management_system.model.Seller;
import com.example.campus_mess_coupon_management_system.repository.CouponRepository;
import com.example.campus_mess_coupon_management_system.repository.SellerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public Coupon addCoupon(Coupon coupon) {
        // Get the logged-in seller's email from the security context
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found with email: " + email));

        coupon.setSeller(seller);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllActiveCoupons() {
        return couponRepository.findByIsActiveTrue();
    }

    public Coupon updateCouponStatus(Long id, boolean isActive) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
        coupon.setIsActive(isActive);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getCouponsBySellerId(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        return couponRepository.findBySeller(seller);
    }

    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
    }

    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
        couponRepository.delete(coupon);
    }

    public Coupon updateCouponDetails(Long id, Coupon updatedCouponDetails) {
        // Find the existing coupon
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));

        // Update fields only if they are provided
        if (updatedCouponDetails.getMealSlot() != null) {
            existingCoupon.setMealSlot(updatedCouponDetails.getMealSlot());
        }
        if (updatedCouponDetails.getDate() != null) {
            existingCoupon.setDate(updatedCouponDetails.getDate());
        }
        if (updatedCouponDetails.getPrice() != null) {
            existingCoupon.setPrice(updatedCouponDetails.getPrice());
        }
        if (updatedCouponDetails.getIsActive() != null) {
            existingCoupon.setIsActive(updatedCouponDetails.getIsActive());
        }
        return couponRepository.save(existingCoupon);
    }


    public List<Coupon> filterCoupons(LocalDate date, String mealSlot, Double minPrice, Double maxPrice) {
        // Handle the filtering logic
        if (date != null && mealSlot != null && minPrice != null && maxPrice != null) {
            return couponRepository.findByDateAndMealSlotAndPriceBetween(date, mealSlot, minPrice, maxPrice);
        } else if (date != null && minPrice != null && maxPrice != null) {
            return couponRepository.findByDateAndPriceBetween(date, minPrice, maxPrice);
        } else if (mealSlot != null && minPrice != null && maxPrice != null) {
            return couponRepository.findByMealSlotAndPriceBetween(mealSlot, minPrice, maxPrice);
        } else if (minPrice != null && maxPrice != null) {
            return couponRepository.findByPriceBetween(minPrice, maxPrice);
        } else if (date != null && mealSlot != null) {
            return couponRepository.findByDateAndMealSlot(date, mealSlot);
        } else if (date != null) {
            return couponRepository.findByDate(date);
        } else if (mealSlot != null) {
            return couponRepository.findByMealSlot(mealSlot);
        } else {
            return couponRepository.findAll();
        }
    }
}

