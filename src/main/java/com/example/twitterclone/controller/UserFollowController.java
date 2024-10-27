package com.example.twitterclone.controller;

import com.example.twitterclone.model.User;
import com.example.twitterclone.model.UserFollow;
import com.example.twitterclone.service.UserFollowService;
import com.example.twitterclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private UserService userService;

    // ユーザーをフォローするエンドポイント
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable Long userId, Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);
        // フォロー処理を実行
        userFollowService.followUser(currentUser.getId(), userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "ユーザーID: " + currentUser.getId() + " がユーザーID: " + userId + " をフォローしました。");
        return ResponseEntity.ok(response);
    }
    // ユーザーのフォローを解除するエンドポイント
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long userId, Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);
        // フォロー解除処理を実行
        userFollowService.unfollowUser(currentUser.getId(), userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "ユーザーID: " + currentUser.getId() + " がユーザーID: " + userId + " のフォローを解除しました。");
        return ResponseEntity.ok(response);
    }

    // 現在のユーザーがフォローしているユーザーのリストを取得するエンドポイント
    @GetMapping("/following")
    public ResponseEntity<List<UserFollow>> getFollowing(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);
        // フォローしているユーザーのリストを取得
        List<UserFollow> following = userFollowService.getFollowing(currentUser.getId());
        return ResponseEntity.ok(following);
    }

    // 現在のユーザーをフォローしているユーザーのリストを取得するエンドポイント
    @GetMapping("/followers")
    public ResponseEntity<List<UserFollow>> getFollowers(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);
        // ォロワーのリストを取得
        List<UserFollow> followers = userFollowService.getFollowers(currentUser.getId());
        return ResponseEntity.ok(followers);
    }

    // 特定のユーザーをフォローしているか確認するエンドポイント
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long userId, Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);
        // フォロー状態を確認
        boolean isFollowing = userFollowService.isFollowing(currentUser.getId(), userId);
        return ResponseEntity.ok(isFollowing);
    }

    // 認証情報から現在のユーザーを取得するヘルパーメソッド
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }
}
