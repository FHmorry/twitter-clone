package com.example.twitterclone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// WebMvcConfigurerを実装するクラス
public class WebConfig implements WebMvcConfigurer {
    @Override
    // CORSの設定を追加するメソッド
    public void addCorsMappings(CorsRegistry registry) {
        // 全てのパスに対してCORSを許可
        registry.addMapping("/**")
                // 許可するオリジンを設定
                .allowedOrigins("http://localhost:3000")
                // 許可するHTTPメソッドを設定
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 許可するヘッダーを設定
                .allowedHeaders("*")
                // 資格情報を許可
                .allowCredentials(true);
    }
}