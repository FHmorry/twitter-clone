package com.example.twitterclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.twitterclone.model.User;
import com.example.twitterclone.repository.UserRepository;

@RestController
public class LoginController {

  @Autowired
  private UserRepository userRepository;

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
        .map(foundUser -> {
            return ResponseEntity.ok().body("Login successful");
        })
        .orElseGet(() -> {
            return ResponseEntity.status(401).body("Login failed");
        });
  }
}
