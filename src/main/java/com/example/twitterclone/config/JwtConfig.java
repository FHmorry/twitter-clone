package com.example.twitterclone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secretKey;
    private long expirationMilliseconds;

    // ゲッターとセッター
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getExpirationMilliseconds() {
        return expirationMilliseconds;
    }

    public void setExpirationMilliseconds(long expirationMilliseconds) {
        this.expirationMilliseconds = expirationMilliseconds;
    }
}
