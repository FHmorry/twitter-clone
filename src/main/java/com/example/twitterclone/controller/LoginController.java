package com.example.twitterclone.controller;

import com.example.twitterclone.security.JwtTokenProvider;
import com.example.twitterclone.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    /**
     * ログインのエンドポイント
     * @param loginRequest ログイン情報
     * @return ログイン結果とJWTトークン
     */
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);

            logger.info("ログイン成功: {}", username);

            Map<String, String> response = new HashMap<>();
            response.put("message", "ログイン成功");
            response.put("token", token);
            response.put("user", userService.findByUsername(username).getId().toString()); // 必要に応じてユーザー情報も含める

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("ログイン失敗: {} - 理由: {}", loginRequest.get("username"), e.getMessage());
            
            // 認証失敗時のエラーレスポンスを作成
            Map<String, String> response = new HashMap<>();
            response.put("message", "ログイン失敗: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
}
