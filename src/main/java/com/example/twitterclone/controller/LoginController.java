package com.example.twitterclone.controller;

import com.example.twitterclone.security.JwtTokenProvider;
import com.example.twitterclone.service.UserService;
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

    private final AuthenticationManager authenticationManager; // 認証マネージャー
    private final JwtTokenProvider jwtTokenProvider; // JWTトークンプロバイダー
    private final UserService userService; // ユーザーサービス

    // コンストラクタインジェクション
    public LoginController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    /**
     * ログインのエンドポイント
     * @param loginRequest ログイン情報
     * @return ログイン結果とJWTトークン
     */
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            // リクエストからユーザー名とパスワードを取得
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            // ユーザー名とパスワードを使用して認証を試みる
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // 認証が成功した場合、セキュリティコンテキストに認証情報を設定
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // JWTトークンを生成
            String token = jwtTokenProvider.generateToken(authentication);

            // レスポンスにログイン成功メッセージとトークンを含める
            Map<String, String> response = new HashMap<>();
            response.put("message", "ログイン成功");
            response.put("token", token);
            response.put("user", userService.findByUsername(username).getId().toString()); // 必要に応じてユーザー情報も含める

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // 認証失敗時のエラーレスポンスを作成
            Map<String, String> response = new HashMap<>();
            response.put("message", "ログイン失敗: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
}
