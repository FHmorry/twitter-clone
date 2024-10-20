package com.example.twitterclone.service;

import com.example.twitterclone.model.User;
import com.example.twitterclone.repository.UserRepository;
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
        // ユーザー名を使用してデータベースからユーザーを検索
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Spring SecurityのUserDetailsオブジェクトを作成して返す
        // パラメータ: ユーザー名、パスワード、権限リスト（ここでは空のリスト）
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
        // 同じユーザー名が既に存在するかチェック
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("ユーザー名 '" + user.getUsername() + "' は既に使用されています。");
        }

        // パスワードをハッシュ化
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // ユーザーを保存
        return userRepository.save(user);
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
}
