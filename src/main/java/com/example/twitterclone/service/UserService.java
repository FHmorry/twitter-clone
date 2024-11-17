package com.example.twitterclone.service;

import com.example.twitterclone.model.User;
import com.example.twitterclone.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;

    // コンストラクタ: UserRepository, PasswordEncoder, PostServiceを初期化
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       PostService postService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postService = postService;
    }

    /**
     * ユーザー登録を行うメソッド
     * @param user 登録するユーザー情報
     * @return 登録されたユーザーエンティティ
     */
    @Transactional
    public User registerUser(User user) {
        // パスワードをハッシュ化
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            // ユーザー情報をデータベースに保存
            User savedUser = userRepository.save(user);
            logger.info("ユーザー登録成功: {}", savedUser.getUsername());
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            // ユーザー登録失敗時のエラーログ
            logger.error("ユーザー登録失敗: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 現在認証されているユーザーを取得するメソッド
     * @param authentication 認証情報
     * @return 現在のユーザーエンティティ
     */
    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        // ユーザー名からユーザー情報を取得
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }

    /**
     * ユーザーIDからユーザーを取得するメソッド
     * @param userId ユーザーID
     * @return ユーザーエンティティ
     */
    public User findById(Long userId) {
        // ユーザーIDからユーザー情報を取得
        return userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: ID " + userId));
    }

        /**
     * ユーザー名からユーザーを取得するメソッド
     * @param username ユーザー名
     * @return ユーザーエンティティ
     */
    public User findByUsername(String username) {
        // ユーザー名からユーザー情報を取得
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }

    /**
     * ユーザーの投稿数を取得するメソッド
     * @param userId ユーザーID
     * @return 投稿数
     */
    public int getPostCount(Long userId) {
        // ユーザーIDに基づいて投稿数をカウント
        return postService.countPostsByUserId(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ユーザー名からユーザー情報を取得
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
        
        // UserDetailsオブジェクトを生成して返す
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities("ROLE_USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }



    /**
     * ユーザーIDからUserDetailsを取得するメソッド
     * @param id ユーザーID
     * @return UserDetailsオブジェクト
     */
    public UserDetails loadUserById(Long id) {
        // ユーザーIDからユーザー情報を取得
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        // UserDetailsオブジェクトを生成して返す
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            new ArrayList<>()
        );
    }

}
