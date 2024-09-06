package com.example.campus_mess_coupon_management_system.repository;

import com.example.campus_mess_coupon_management_system.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface SellerRepository extends JpaRepository<Seller, Long> {
   Optional<Seller> findByEmail(String email);
}

