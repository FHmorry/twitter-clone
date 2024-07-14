package com.example.twitterclone.config;

import com.example.twitterclone.repository.UserRepository;
import com.example.twitterclone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;

@Configuration 
@EnableWebSecurity 
public class SecurityConfig {

    @Autowired 
    private UserRepository userRepository;

    // SecurityFilterChainのBeanを定義する
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CORSを有効にする
        http.cors(cors -> cors.and())  
            // CSRFを無効にする
            .csrf(csrf -> csrf.disable()) 
            // 認証の設定
            .authorizeHttpRequests(auth -> auth
                // /loginと/logoutは認証不要
                .requestMatchers("/login", "/logout").permitAll() 
                // その他のリクエストは認証が必要
                .anyRequest().authenticated() 
            )
            // ログアウトの設定
            .logout(logout -> logout
                // ログアウトURLを設定
                .logoutUrl("/logout") 
                // ログアウト成功時のステータスを設定
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK); 
                })
            );
        // SecurityFilterChainを構築して返す
        return http.build(); 
    }

    // UserDetailsServiceのBeanを定義する
    @Bean 
    public UserDetailsService userDetailsService() {
        return username -> {
            // ユーザー名でユーザーを検索
            User user = userRepository.findByUsername(username); 
            if (user != null) {
                // ユーザーが見つかった場合、UserDetailsを返す
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>()); 
            } else {
                // ユーザーが見つからない場合、例外をスローする
                throw new UsernameNotFoundException("User not found"); 
            }
        };
    }
}