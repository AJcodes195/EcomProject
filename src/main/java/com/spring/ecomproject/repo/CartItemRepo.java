package com.spring.ecomproject.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecomproject.entity.CartItem;
import com.spring.ecomproject.entity.User;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
}
