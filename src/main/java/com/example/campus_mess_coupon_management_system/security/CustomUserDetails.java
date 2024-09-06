package com.example.campus_mess_coupon_management_system.security;

import com.example.campus_mess_coupon_management_system.model.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Seller seller;

    public CustomUserDetails(Seller seller) {
        this.seller = seller;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(seller.getRole()));
    }

    @Override
    public String getPassword() {
        return seller.getPassword();
    }

    @Override
    public String getUsername() {
        return seller.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
