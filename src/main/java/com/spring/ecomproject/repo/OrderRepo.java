package com.spring.ecomproject.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecomproject.entity.OrderEntity;
import com.spring.ecomproject.entity.User;

public interface OrderRepo extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUser(User user);
}
