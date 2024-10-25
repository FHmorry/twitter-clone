package com.example.twitterclone.repository;

import com.example.twitterclone.model.UserFollow;
import com.example.twitterclone.model.UserFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ユーザーのフォロー関係を管理するリポジトリインターフェース
@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {
    // 指定されたユーザーが他のユーザーをフォローしているか確認するメソッド
    boolean existsById_FollowingUserIdAndId_FollowedUserId(Long followingUserId, Long followedUserId);
    
    // 指定されたユーザーのフォロー関係を削除するメソッド
    void deleteById_FollowingUserIdAndId_FollowedUserId(Long followingUserId, Long followedUserId);
    
    // 指定されたユーザーがフォローしているユーザーのリストを取得するメソッド
    List<UserFollow> findById_FollowingUserId(Long followingUserId);
    
    // 指定されたユーザーをフォローしているユーザーのリストを取得するメソッド
    List<UserFollow> findById_FollowedUserId(Long followedUserId);
}
