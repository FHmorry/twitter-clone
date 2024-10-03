package com.example.twitterclone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Securityの設定を行うクラス
 * アプリケーションのセキュリティ設定を定義し、認証・認可の動作を制御する
 */
@Configuration 
@EnableWebSecurity 
public class SecurityConfig {

    @Autowired
    private DaoAuthenticationProvider authenticationProvider;

    /**
     * セキュリティフィルターチェーンを構成するメソッド
     * HTTPリクエストに対するセキュリティ設定を行い、認証・認可のルールを定義する
     * @param http HttpSecurityオブジェクト
     * @return 構成されたSecurityFilterChain
     * @throws Exception セキュリティ設定中に例外が発生した場合
     */
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http)) // CORSの設定を有効化
            .csrf(csrf -> csrf.disable()) // CSRF保護を無効化（RESTful APIの場合は一般的）
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/register").permitAll() // これらのパスは認証不要
                .anyRequest().authenticated() // その他のリクエストは認証が必要
            )
            .formLogin(form -> form.disable()) // ログイン処理を無効化
            .logout(logout -> logout.disable()) // ログアウト処理を無効化
            .authenticationProvider(authenticationProvider);
        return http.build();
    }
}