package com.example.twitterclone.controller;

import com.example.twitterclone.dto.UserFollowResponseDTO;
import com.example.twitterclone.model.User;
import com.example.twitterclone.model.UserFollow;
import com.example.twitterclone.service.UserFollowService;
import com.example.twitterclone.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/follow")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserFollowController.class);

    /**
     * ユーザーをフォローするエンドポイント
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable Long userId, Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = userService.getCurrentUser(authentication);
        // フォロー処理を実行
        userFollowService.followUser(currentUser.getId(), userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "ユーザーID: " + currentUser.getId() + " がユーザーID: " + userId + " をフォローしました。");
        return ResponseEntity.ok(response);
    }

    /**
     * ユーザーのフォローを解除するエンドポイント
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long userId, Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = userService.getCurrentUser(authentication);
        // フォロー解除処理を実行
        userFollowService.unfollowUser(currentUser.getId(), userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "ユーザーID: " + currentUser.getId() + " がユーザーID: " + userId + " のフォローを解除しました。");
        return ResponseEntity.ok(response);
    }

    /**
     * 現在のユーザーがフォローしているユーザーのリストを取得するエンドポイント
     */
    @GetMapping("/following")
    public ResponseEntity<List<UserFollowResponseDTO>> getFollowing(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = userService.getCurrentUser(authentication);
        // フォローしているユーザーのリストを取得
        List<UserFollow> following = userFollowService.getFollowing(currentUser.getId());

        // DTOへの変換
        List<UserFollowResponseDTO> response = following.stream()
            .map(follow -> {
                User followedUser = userService.findById(follow.getFollowedUserId());
                UserFollowResponseDTO dto = UserFollowResponseDTO.fromEntity(followedUser, follow);
                // isFollowingBack や postCount を設定
                dto.setIsFollowingBack(userFollowService.isFollowingBack(currentUser.getId(), followedUser.getId()));
                dto.setPostCount(userService.getPostCount(followedUser.getId()));
                // DTOの内容をログに出力
                logger.debug("DTO: {}", dto);
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * 現在のユーザーをフォローしているユーザーのリストを取得するエンドポイント
     */
    @GetMapping("/followers")
    public ResponseEntity<List<UserFollowResponseDTO>> getFollowers(Authentication authentication) {
        // 現在のユーザーを取得
        User currentUser = userService.getCurrentUser(authentication);
        // フォロワーのリストを取得
        List<UserFollow> followers = userFollowService.getFollowers(currentUser.getId());

        // DTOへの変換
        List<UserFollowResponseDTO> response = followers.stream()
            .map(follow -> {
                User follower = userService.findById(follow.getFollowingUserId());
                UserFollowResponseDTO dto = UserFollowResponseDTO.fromEntity(follower, follow);
                // isFollowingBack や postCount を設定
                dto.setIsFollowingBack(userFollowService.isFollowingBack(follower.getId(), currentUser.getId()));
                dto.setPostCount(userService.getPostCount(follower.getId()));
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
