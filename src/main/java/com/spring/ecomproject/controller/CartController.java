package com.spring.ecomproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.spring.ecomproject.entity.CartItem;
import com.spring.ecomproject.entity.Product;
import com.spring.ecomproject.entity.User;
import com.spring.ecomproject.repo.CartItemRepo;
import com.spring.ecomproject.repo.ProductRepo;
import com.spring.ecomproject.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemRepo cartItemRepository;
    private final ProductRepo productRepository;
    private final UserRepo userRepository;

    @GetMapping
    public List<CartItem> getCart(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return cartItemRepository.findByUser(user);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            Authentication auth,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        CartItem item = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        cartItemRepository.save(item);
        return ResponseEntity.ok("Item added to cart");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(
            Authentication auth,
            @RequestParam Long cartItemId,
            @RequestParam int quantity
    ) {
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return ResponseEntity.ok("Cart item updated");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long id) {
        cartItemRepository.deleteById(id);
        return ResponseEntity.ok("Item removed from cart");
    }
}
