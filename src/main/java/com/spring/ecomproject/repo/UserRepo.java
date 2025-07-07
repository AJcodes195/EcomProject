package com.spring.ecomproject.repo;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecomproject.entity.*;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}






