package com.example.twitterclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.twitterclone.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Date; // Dateクラスをインポート
import io.jsonwebtoken.Jwts; // Jwtsクラスをインポート
import io.jsonwebtoken.SignatureAlgorithm; // SignatureAlgorithmクラスをインポート

/**
 * ログイン処理を担当するコントローラークラス
 * このクラスはユーザーの認証とログイン処理を行います
 */
@RestController
public class LoginController {

  /**
   * Spring Securityの認証マネージャー
   * ユーザーの認証処理に使用されます
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * ログイン処理を行うメソッド
   * @param user ログイン情報を含むユーザーオブジェクト
   * @param request HTTPリクエスト
   * @return ログイン結果を含むResponseEntity
   */
  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletRequest request) {
    try {
      // ユーザー名とパスワードを使用して認証を試みる
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
      
      // 認証成功時、SecurityContextに認証情報を設定
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // アクセストークンを生成
      String token = generateToken(authentication);

      Map<String, String> response = new HashMap<>();
      response.put("message", "ログイン成功");
      response.put("token", token); // アクセストークンをレスポンスに追加
      return ResponseEntity.ok().body(response);
    } catch (AuthenticationException e) {
      // 認証失敗時のエラーレスポンスを作成
      Map<String, String> response = new HashMap<>();
      response.put("message", "ログイン失敗: " + e.getMessage());
      return ResponseEntity.status(401).body(response);
    }
  }

  @Value("${JWT_SECRET_KEY}")
  private String secretKey;

  private String generateToken(Authentication authentication) {
    // JWTトークンを生成
    String username = authentication.getName();
    long expirationTime = 1000 * 60 * 60; // 1時間の有効期限
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationTime);

    // JWTトークンを生成
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, secretKey) // 環境変数から取得した秘密鍵を使用
        .compact();
  }
}
