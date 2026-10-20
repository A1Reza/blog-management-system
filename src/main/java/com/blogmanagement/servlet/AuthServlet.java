package com.blogmanagement.servlet;

import com.blogmanagement.dto.request.UserLoginRequestDto;
import com.blogmanagement.dto.request.UserRegisterRequestDto;
import com.blogmanagement.dto.response.UserResponseDto;
import com.blogmanagement.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends BaseServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String path = request.getPathInfo();

        try {
            if ("/register".equals(path)) {

                UserRegisterRequestDto dto =
                        readRequest(request, UserRegisterRequestDto.class);

                UserResponseDto user = userService.register(dto);

                sendJson(response, HttpServletResponse.SC_CREATED, user);

            } else if ("/login".equals(path)) {

                UserLoginRequestDto dto =
                        readRequest(request, UserLoginRequestDto.class);

                UserResponseDto user = userService.login(dto);

                HttpSession oldSession = request.getSession(false);

                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.id());

                sendJson(response, HttpServletResponse.SC_OK, user);

            } else if ("/logout".equals(path)) {

                HttpSession session = request.getSession(false);

                if (session != null) {
                    session.invalidate();
                }

                sendJson(response, HttpServletResponse.SC_OK,
                        Map.of("message", "Logged out successfully"));

            } else {
                sendJson(response, HttpServletResponse.SC_NOT_FOUND,
                        Map.of("message", "Endpoint not found"));
            }

        } catch (IllegalArgumentException e) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    Map.of("message", e.getMessage()));
        }
    }
}
