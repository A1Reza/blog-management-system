package com.blogmanagement.command;

import com.blogmanagement.dto.response.PostResponseDto;
import com.blogmanagement.entity.Post;

public class PostToPostResponseDto {

    public static PostResponseDto convert(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
