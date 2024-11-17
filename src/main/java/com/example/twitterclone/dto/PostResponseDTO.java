package com.example.twitterclone.dto;

import com.example.twitterclone.model.Post;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserResponseDTO user;

    // エンティティからDTOへの変換メソッド
    public static PostResponseDTO fromEntity(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContents());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUser(UserResponseDTO.fromEntity(post.getUser()));
        return dto;
    }
} 