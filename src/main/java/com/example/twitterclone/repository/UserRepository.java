package com.example.twitterclone.repository;

import com.example.twitterclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}