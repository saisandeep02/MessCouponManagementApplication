package com.example.campus_mess_coupon_management_system.repository;
import com.example.campus_mess_coupon_management_system.model.Seller;
import com.example.campus_mess_coupon_management_system.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByIsActiveTrue();
    List<Coupon> findBySeller(Seller seller);  // Method to find coupons by seller
    List<Coupon> findByDateAndMealSlot(LocalDate date, String mealSlot);
    List<Coupon> findByDate(LocalDate date);
    List<Coupon> findByMealSlot(String mealSlot);
    List<Coupon> findByDateAndMealSlotAndPriceBetween(LocalDate date, String mealSlot, Double minPrice, Double maxPrice);
    List<Coupon> findByDateAndPriceBetween(LocalDate date, Double minPrice, Double maxPrice);
    List<Coupon> findByMealSlotAndPriceBetween(String mealSlot, Double minPrice, Double maxPrice);
    List<Coupon> findByPriceBetween(Double minPrice, Double maxPrice);
}
