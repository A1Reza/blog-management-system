
package com.blogmanagement.dto.response;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
