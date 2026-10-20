
package com.blogmanagement.command;

import com.blogmanagement.dto.request.UserRegisterRequestDto;
import com.blogmanagement.entity.User;

public class UserRegisterRequestDtoToUser {

    public static User convert(UserRegisterRequestDto dto) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .build();
    }
}
