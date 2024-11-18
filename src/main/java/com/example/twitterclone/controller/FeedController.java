package com.example.twitterclone.controller;

import com.example.twitterclone.dto.PostResponseDTO;
import com.example.twitterclone.model.Post;
import com.example.twitterclone.model.User;
import com.example.twitterclone.service.PostService;
import com.example.twitterclone.service.UserFollowService;
import com.example.twitterclone.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final UserFollowService userFollowService;
    private final PostService postService;
    private final UserService userService;

    public FeedController(UserFollowService userFollowService, PostService postService, UserService userService) {
        this.userFollowService = userFollowService;
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * フォローしているユーザーの投稿を取得するエンドポイント
     * GET /api/feed/following-posts
     */
    @GetMapping("/following-posts")
    public ResponseEntity<List<PostResponseDTO>> getFollowingPosts(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = userService.getCurrentUser(authentication);

        // フォローしているユーザーのIDリストを取得
        List<Long> followingUserIds = userFollowService.getFollowingUserIds(currentUser.getId());

        // フォローしているユーザーの投稿を一括取得
        List<Post> posts = postService.getPostsByUserIdsWithUser(followingUserIds);

        // DTOへの変換
        List<PostResponseDTO> response = posts.stream()
            .map(PostResponseDTO::fromEntity)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * 自分とフォローしているユーザーの投稿を取得するエンドポイント
     * GET /api/feed/my-and-following-posts
     */
    @GetMapping("/my-and-following-posts")
    public ResponseEntity<List<PostResponseDTO>> getMyAndFollowingPosts(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = userService.getCurrentUser(authentication);

        // フォローしているユーザーのIDリストを取得
        List<Long> followingUserIds = userFollowService.getFollowingUserIds(currentUser.getId());

        // 自分のユーザーIDをリストに追加
        followingUserIds.add(currentUser.getId());

        // 自分とフォローしているユーザーの投稿を一括取得
        List<Post> posts = postService.getPostsByUserIdsWithUser(followingUserIds);

        // DTOへの変換
        List<PostResponseDTO> response = posts.stream()
            .map(PostResponseDTO::fromEntity)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
