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
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("既に同一名のユーザーが存在します。");
        }

        userRepository.save(newUser);

        return ResponseEntity.ok("ユーザー登録が完了しました。");
    }
}
