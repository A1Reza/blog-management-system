package com.blogmanagement.command;

import com.blogmanagement.dto.response.UserResponseDto;
import com.blogmanagement.entity.User;

public class UserToUserResponseDto {

    public static UserResponseDto convert(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
