package com.spring.ecomproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.spring.ecomproject.entity.CartItem;
import com.spring.ecomproject.entity.OrderEntity;
import com.spring.ecomproject.entity.OrderItem;
import com.spring.ecomproject.entity.User;
import com.spring.ecomproject.repo.CartItemRepo;
import com.spring.ecomproject.repo.OrderRepo;
import com.spring.ecomproject.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepo orderRepository;
    private final CartItemRepo cartItemRepository;
    private final UserRepo userRepository;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<String> createOrder(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> OrderItem.builder()
                        .product(ci.getProduct())
                        .quantity(ci.getQuantity())
                        .build())
                .toList();

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .totalAmount(total)
                .items(orderItems)
                .build();

        orderRepository.save(order);
        cartItemRepository.deleteByUser(user);

        return ResponseEntity.ok("Order created successfully");
    }

    @GetMapping
    public List<OrderEntity> getOrders(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return orderRepository.findByUser(user);
    }
}
