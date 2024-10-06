package com.example.twitterclone.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ユーザー情報を表すエンティティクラス
 */
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false, length = 254, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * デフォルトコンストラクタ
     */
    public User() {
    }

    /**
     * IDを取得する
     * @return ユーザーID
     */
    public Long getId() {
        return id;
    }

    /**
     * IDを設定する
     * @param id 設定するユーザーID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ユーザー名を取得する
     * @return ユーザー名
     */
    public String getUsername() {
        return username;
    }

    /**
     * ユーザー名を設定する
     * @param username 設定するユーザー名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * メールアドレスを取得する
     * @return メールアドレス
     */
    public String getEmail() {
        return email;
    }

    /**
     * メールアドレスを設定する
     * @param email 設定するメールアドレス
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * パスワードを取得する
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定する
     * @param password 設定するパスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 作成日時を取得する
     * @return 作成日時
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 更新日時を取得する
     * @return 更新日時
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * エンティティ作成時に自動的に呼び出され、作成日時と更新日時を設定する
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * エンティティ更新時に自動的に呼び出され、更新日時を設定する
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
