package com.example.twitterclone.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.example.twitterclone.model.User;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 相互フォロー状態や投稿数など、必要に応じて追加
    private boolean isFollowingBack;
    private int postCount;

    // エンティティからDTOへの変換メソッド
    public static UserResponseDTO fromEntity(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        // isFollowingBack や postCount はサービス層で設定
        return dto;
    }
} 