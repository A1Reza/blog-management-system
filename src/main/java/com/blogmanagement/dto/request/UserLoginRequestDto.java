package com.blogmanagement.dto.request;

public record UserLoginRequestDto(
        String username,
        String password
) {
}
