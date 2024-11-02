package com.example.twitterclone.controller;

import com.example.twitterclone.model.Post;
import com.example.twitterclone.model.User;
import com.example.twitterclone.service.PostService;
import com.example.twitterclone.service.UserFollowService;
import com.example.twitterclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * フォローしているユーザーの投稿を取得するエンドポイント
     * GET /api/feed/following-posts
     */
    @GetMapping("/following-posts")
    public ResponseEntity<List<Post>> getFollowingPosts(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);

        // フォローしているユーザーのIDリストを取得
        List<Long> followingUserIds = userFollowService.getFollowingUserIds(currentUser.getId());

        // フォローしているユーザーの投稿を一括取得
        List<Post> posts = postService.getPostsByUserIdsWithUser(followingUserIds);

        return ResponseEntity.ok(posts);
    }

    /**
     * 自分とフォローしているユーザーの投稿を取得するエンドポイント
     * GET /api/feed/my-and-following-posts
     */
    @GetMapping("/my-and-following-posts")
    public ResponseEntity<List<Post>> getMyAndFollowingPosts(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = getCurrentUser(authentication);

        // フォローしているユーザーのIDリストを取得
        List<Long> followingUserIds = userFollowService.getFollowingUserIds(currentUser.getId());

        // 自分のユーザーIDをリストに追加
        followingUserIds.add(currentUser.getId());

        // 自分とフォローしているユーザーの投稿を一括取得
        List<Post> posts = postService.getPostsByUserIdsWithUser(followingUserIds);

        return ResponseEntity.ok(posts);
    }

    /**
     * 認証情報から現在のユーザーを取得するヘルパーメソッド
     */
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }
}
