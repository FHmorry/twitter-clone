package com.example.twitterclone.security;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import com.example.twitterclone.config.JwtConfig;
import com.example.twitterclone.service.UserService;
import com.example.twitterclone.model.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;
    private final UserService userService; // 追加

    public JwtTokenProvider(JwtConfig jwtConfig, UserDetailsService userDetailsService, UserService userService) { // コンスラクタに追加
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    // 認証情報からJWTトークンを生成するメソッド
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // 認証情報からユーザー詳細を取得
        User user = userService.findByUsername(userDetails.getUsername()); // ユーザー情報を取得
        
        Claims claims = Jwts.claims();
        claims.setSubject(user.getId().toString());
        
        // ユーザー情報をclaimsに追加
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("username", user.getUsername());
        claims.putAll(additionalInfo);
        
        Date now = new Date(); // 現在の日時を取得
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationMilliseconds()); // 有効期限を設定
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey())
                .compact();
    }

    // HTTPリクエストからJWTトークンを抽出するメソッド
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer "を除去してトークンを返す
        }
        return null; // トークンが存在しない場合はnullを返す
    }

    // JWTトークンの有効性を検証するメソッド
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
            .setSigningKey(jwtConfig.getSecretKey())
            .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // エラーハンドリング（必要に応じてログ出力など）
        }
        return false;
    }

    // JWTトークンからAuthenticationオブジェクトを取得するメソッド
    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
                
            Long userId = Long.parseLong(claims.getSubject());
            String username = (String) claims.get("username");
            UserDetails userDetails = userService.loadUserById(userId);
            
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (Exception e) {
            // エラーハンドリング（必要に応じてログ出力など）
            return null;
        }
    }
}
