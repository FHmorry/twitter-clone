package com.example.twitterclone.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// ユーザーのフォロー関係を表すエンティティクラス
@Entity
@Table(name = "user_follows")
public class UserFollow {

    @EmbeddedId
    private UserFollowId id;

    // フォローした日時
    @Column(name = "follow_date")
    private LocalDateTime followDate;

    // デフォルトコンストラクタ
    public UserFollow() {}

    // フォロー関係を初期化するコンストラクタ
    public UserFollow(Long followingUserId, Long followedUserId) {
        this.id = new UserFollowId(followingUserId, followedUserId);
        this.followDate = LocalDateTime.now(); // フォロー日時を現在の日時に設定
    }

    // フォロー関係を取得するメソッド
    public UserFollowId getId() {
        return id;
    }

    // フォロー関係を設定するメソッド
    public void setId(UserFollowId id) {
        this.id = id;
    }

    // フォローしているユーザーのIDを取得するメソッド
    public Long getFollowingUserId() {
        return id.getFollowingUserId();
    }

    // フォローされているユーザーのIDを取得するメソッド
    public Long getFollowedUserId() {
        return id.getFollowedUserId();
    }

    // フォロー日時を取得するメソッド
    public LocalDateTime getFollowDate() {
        return followDate;
    }

    // フォロー日時を設定するメソッド
    public void setFollowDate(LocalDateTime followDate) {
        this.followDate = followDate;
    }
}
