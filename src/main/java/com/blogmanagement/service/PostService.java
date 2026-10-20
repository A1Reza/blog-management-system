package com.blogmanagement.service;

import com.blogmanagement.command.PostToPostResponseDto;
import com.blogmanagement.dto.request.PostRequestDto;
import com.blogmanagement.dto.response.PostResponseDto;
import com.blogmanagement.entity.Post;
import com.blogmanagement.entity.User;
import com.blogmanagement.repository.PostRepository;
import com.blogmanagement.repository.UserRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepository = new PostRepository();
    private final UserRepository userRepository = new UserRepository();

    public List<PostResponseDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostToPostResponseDto::convert)
                .toList();
    }

    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Post not found"));

        return PostToPostResponseDto.convert(post);
    }

    public PostResponseDto create(PostRequestDto dto, Long userId) {

        validatePost(dto);

        User author = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Post post = Post.builder()
                .title(dto.title())
                .content(dto.content())
                .author(author)
                .build();

        return PostToPostResponseDto.convert(postRepository.save(post));
    }

    public PostResponseDto update(Long postId, PostRequestDto dto, Long userId) {

        validatePost(dto);

        Post post = findPostOrThrow(postId);
        checkOwnership(post, userId);

        post.setTitle(dto.title());
        post.setContent(dto.content());

        return postRepository.updateAndConvert(post);
    }

    public void delete(Long postId, Long userId) {

        Post post = findPostOrThrow(postId);
        checkOwnership(post, userId);

        postRepository.deleteById(postId);
    }

    private Post findPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Post not found"));
    }

    private void checkOwnership(Post post, Long userId) {

        if (!post.getAuthor().getId().equals(userId)) {
            throw new SecurityException(
                    "You are not allowed to modify this post");
        }
    }

    private void validatePost(PostRequestDto dto) {

        if (dto.title() == null || dto.title().isBlank()
                || dto.content() == null || dto.content().isBlank()) {
            throw new IllegalArgumentException(
                    "Title and content are required");
        }
    }
}
