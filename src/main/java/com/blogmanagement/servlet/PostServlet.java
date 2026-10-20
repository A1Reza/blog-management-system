package com.blogmanagement.servlet;

import com.blogmanagement.dto.request.PostRequestDto;
import com.blogmanagement.dto.response.PostResponseDto;
import com.blogmanagement.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/posts/*")
public class PostServlet extends BaseServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            String path = request.getPathInfo();

            if (path == null || "/".equals(path)) {
                List<PostResponseDto> posts = postService.findAll();
                sendJson(response, HttpServletResponse.SC_OK, posts);
                return;
            }

            Long id = Long.parseLong(path.substring(1));

            sendJson(response, HttpServletResponse.SC_OK,
                    postService.findById(id));

        } catch (NumberFormatException e) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    Map.of("message", "Invalid post ID"));
        } catch (IllegalArgumentException e) {
            sendJson(response, HttpServletResponse.SC_NOT_FOUND,
                    Map.of("message", e.getMessage()));
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            PostRequestDto dto =
                    readRequest(request, PostRequestDto.class);

            Long userId = (Long) request.getSession(false)
                    .getAttribute("userId");

            sendJson(response, HttpServletResponse.SC_CREATED,
                    postService.create(dto, userId));

        } catch (IllegalArgumentException e) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    Map.of("message", e.getMessage()));
        }
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            Long id = extractId(request);
            Long userId = (Long) request.getSession(false)
                    .getAttribute("userId");

            PostRequestDto dto =
                    readRequest(request, PostRequestDto.class);

            sendJson(response, HttpServletResponse.SC_OK,
                    postService.update(id, dto, userId));

        } catch (NumberFormatException e) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    Map.of("message", "Invalid post ID"));
        } catch (SecurityException e) {
            sendJson(response, HttpServletResponse.SC_FORBIDDEN,
                    Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            sendJson(response, HttpServletResponse.SC_NOT_FOUND,
                    Map.of("message", e.getMessage()));
        }
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            Long id = extractId(request);
            Long userId = (Long) request.getSession(false)
                    .getAttribute("userId");

            postService.delete(id, userId);

            sendJson(response, HttpServletResponse.SC_OK,
                    Map.of("message", "Post deleted successfully"));

        } catch (NumberFormatException e) {
            sendJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    Map.of("message", "Invalid post ID"));
        } catch (SecurityException e) {
            sendJson(response, HttpServletResponse.SC_FORBIDDEN,
                    Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            sendJson(response, HttpServletResponse.SC_NOT_FOUND,
                    Map.of("message", e.getMessage()));
        }
    }

    private Long extractId(HttpServletRequest request) {
        String path = request.getPathInfo();

        if (path == null || !path.matches("/\\d+")) {
            throw new NumberFormatException("Invalid post ID");
        }

        return Long.parseLong(path.substring(1));
    }
}
