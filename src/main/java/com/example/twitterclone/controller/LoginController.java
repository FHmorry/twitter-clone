package com.example.twitterclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.twitterclone.model.User;
import com.example.twitterclone.repository.UserRepository;
import java.util.Collections; // Added this import
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

  @Autowired
  private UserRepository userRepository;

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletRequest request) {
    return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
        .map(foundUser -> {
            // ログイン成功時にセッションを再作成して再利用を防止
            HttpSession session = request.getSession(true);
            
            // レスポンスボディにユーザー名を含める
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", foundUser.getUsername());
            return ResponseEntity.ok().body(response);
        })
        .orElseGet(() -> {
            // 修正: Collectionsを使用するためにインポートを追加
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "User not found"));
        });
  }
}