
package com.blogmanagement.service;

import com.blogmanagement.command.UserRegisterRequestDtoToUser;
import com.blogmanagement.command.UserToUserResponseDto;
import com.blogmanagement.dto.request.UserLoginRequestDto;
import com.blogmanagement.dto.request.UserRegisterRequestDto;
import com.blogmanagement.dto.response.UserResponseDto;
import com.blogmanagement.entity.User;
import com.blogmanagement.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public UserResponseDto register(UserRegisterRequestDto dto) {

        if (dto.username() == null || dto.username().isBlank()
                || dto.email() == null || dto.email().isBlank()
                || dto.password() == null || dto.password().isBlank()) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = UserRegisterRequestDtoToUser.convert(dto);

        user.setPassword(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));

        User savedUser = userRepository.save(user);

        return UserToUserResponseDto.convert(savedUser);
    }

    public UserResponseDto login(UserLoginRequestDto dto) {

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid username or password"));

        if (!BCrypt.checkpw(dto.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return UserToUserResponseDto.convert(user);
    }
}
