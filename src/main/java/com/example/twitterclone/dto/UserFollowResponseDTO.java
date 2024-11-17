package com.example.twitterclone.dto;

import com.example.twitterclone.model.User;
import com.example.twitterclone.model.UserFollow;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserFollowResponseDTO {
    private UserResponseDTO user;
    private LocalDateTime followDate;
    private boolean isFollowingBack;
    private long postCount;

    // エンティティとユーザー情報からDTOへの変換メソッド
    public static UserFollowResponseDTO fromEntity(User user, UserFollow follow) {
        UserFollowResponseDTO dto = new UserFollowResponseDTO();
        dto.setUser(UserResponseDTO.fromEntity(user));
        dto.setFollowDate(follow.getFollowDate());
        // isFollowingBack はサービス層で設定
        return dto;
    }

    public void setIsFollowingBack(boolean isFollowingBack) {
        this.isFollowingBack = isFollowingBack;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }
} 