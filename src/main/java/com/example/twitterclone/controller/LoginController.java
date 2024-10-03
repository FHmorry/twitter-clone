package com.example.twitterclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
          new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
      );
      // 認証成功時、SecurityContextに認証情報を設定
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // 認証成功時のレスポンスを作成
      Map<String, String> response = new HashMap<>();
      response.put("message", "ログイン成功");
      response.put("username", user.getUsername());
      return ResponseEntity.ok().body(response);
    } catch (AuthenticationException e) {
      // 認証失敗時のエラーレスポンスを作成
      Map<String, String> response = new HashMap<>();
      response.put("message", "ログイン失敗: " + e.getMessage());
      return ResponseEntity.status(401).body(response);
    }
  }
}
