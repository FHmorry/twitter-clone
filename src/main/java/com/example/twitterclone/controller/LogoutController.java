package com.example.twitterclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import com.example.twitterclone.security.JwtTokenProvider;

@RestController
public class LogoutController {

    private final JwtTokenProvider jwtTokenProvider;

    public LogoutController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 認証情報が存在する場合
        if (authentication != null) {
            // JWTトークンを無効化
            String token = jwtTokenProvider.resolveToken(request);
            if (token != null) {
                jwtTokenProvider.invalidateToken(token);
            }
            // セキュリティコンテキストをクリア
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "ログアウトに成功しました");
        
        return ResponseEntity.ok(responseBody);
    }
}