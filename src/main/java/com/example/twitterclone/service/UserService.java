package com.example.twitterclone.service;

import com.example.twitterclone.model.User;
import com.example.twitterclone.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * ユーザー関連のサービスを提供するクラス
 * Spring SecurityのUserDetailsServiceを実装し、認証に必要な情報を提供する
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * コンストラクタ
     * @param userRepository ユーザー情報を操作するリポジトリ
     * @param passwordEncoder パスワードをハッシュ化するエンコーダー
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ユーザー名からUserDetailsを読み込むメソッド
     * @param username 検索対象のユーザー名
     * @return UserDetails オブジェクト
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスロー
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("ユーザー名からユーザーを検索: {}", username);
        
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            
            logger.info("ユーザーが見つかりました: {}", user.getUsername());
            logger.debug("ユーザー詳細 - ID: {}, Email: {}", user.getId(), user.getEmail());
            logger.debug("保存されているパスワードハッシュ: {}", user.getPassword());
            
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
            );
        } catch (UsernameNotFoundException e) {
            logger.error("ユーザーが見つかりません: {}", username);
            throw e;
        }
    }

    /**
     * ユーザーIDからUserDetailsを読み込むメソッド
     * @param userId 検索対象のユーザーID
     * @return UserDetails オブジェクト
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスロー
     */
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        // ユーザーIDを使用してデータベースからユーザーを検索
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        
        // Spring SecurityのUserDetailsオブジェクトを作成して返す
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // 現在、権限は設定していない
        );
    }

    /**
     * ユーザーを登録するメソッド
     * @param user 登録するユーザー情報
     * @return 登録されたユーザー
     * @throws DataIntegrityViolationException 同じユーザー名が既に存在する場合
     */
    public User registerUser(User user) throws DataIntegrityViolationException {
        logger.info("新規ユーザー登録開始: {}", user.getUsername());
        
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("ユーザー名が重複: {}", user.getUsername());
            throw new DataIntegrityViolationException("ユーザー名 '" + user.getUsername() + "' は既に使用されています。");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        User savedUser = userRepository.save(user);
        logger.info("ユーザー登録成功: {}", savedUser.getUsername());
        
        return savedUser;
    }

    /**
     * ユーザー名からユーザーを取得するメソッド
     * @param username 検索対象のユーザー名
     * @return ユーザーオブジェクト
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスロー
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * ユーザーIDからユーザーオブジェクトを取得するメソッド
     * @param userId ユーザーID
     * @return ユーザーオブジェクト
     */
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }
}
