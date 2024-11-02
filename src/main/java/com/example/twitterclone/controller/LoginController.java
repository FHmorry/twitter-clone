package com.example.twitterclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.example.twitterclone.security.JwtTokenProvider;

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
   * JWTトークン生成プロバイダー
   */
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  /**
   * ログイン処理を行うメソッド
   * @param user ログイン情報を含むユーザーオブジェクト
   * @param request HTTPリクエスト
   * @return ログイン結果を含むResponseEntity
   */
  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletRequest request) {
    logger.info("ログインリクエスト受信: {}", user.getUsername());
    
    // パスワードのnullチェックを追加
    if (user.getPassword() == null) {
        logger.error("パスワードがnullです: {}", user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("message", "ログイン失敗: パスワードが入力されていません");
        return ResponseEntity.status(401).body(response);
    }
    
    logger.debug("受信したパスワードの長さ: {}", user.getPassword().length());
    
    try {
      // ユーザー名とパスワードを使用して認証を試みる
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
      
      logger.debug("認証成功 - 認証情報: {}", authentication);
      
      // 認証成功時、SecurityContextに認証情報を設定
      SecurityContextHolder.getContext().setAuthentication(authentication);

      String token = jwtTokenProvider.generateToken(authentication);

      logger.info("ログイン成功: {}", user.getUsername());

      Map<String, String> response = new HashMap<>();
      response.put("message", "ログイン成功");
      response.put("token", token);
      return ResponseEntity.ok().body(response);
    } catch (AuthenticationException e) {
      logger.error("ログイン失敗: {} - 理由: {}", user.getUsername(), e.getMessage());
      
      // 認証失敗時のエラーレスポンスを作成
      Map<String, String> response = new HashMap<>();
      response.put("message", "ログイン失敗: " + e.getMessage());
      return ResponseEntity.status(401).body(response);
    }
  }
}
