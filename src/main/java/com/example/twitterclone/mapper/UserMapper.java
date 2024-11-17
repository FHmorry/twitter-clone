package com.example.twitterclone.mapper;

import com.example.twitterclone.dto.UserResponseDTO;
import com.example.twitterclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDTO toDTO(User user);
} 