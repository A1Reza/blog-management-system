package com.blogmanagement.dto.request;

public record UserRegisterRequestDto(
        String username,
        String email,
        String password
) {
}