package com.example.twitterclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
// LogoutControllerクラス
public class LogoutController {

    @PostMapping("/logout")
    // ログアウト処理を行うメソッド
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 認証情報が存在する場合
        if (authentication != null) {
            // セキュリティコンテキストからログアウトする
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        // レスポンスヘッダーにリダイレクト先を設定
        response.setHeader("Location", "http://localhost:3000/dashboard");
        // 302 Foundステータスを返す(リダイレクトを明示的に宣言)
        return new ResponseEntity<>(HttpStatus.FOUND); // 302 Found
    }
}