package com.example.twitterclone.mapper;

import com.example.twitterclone.dto.PostResponseDTO;
import com.example.twitterclone.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostResponseDTO toDTO(Post post);
} 