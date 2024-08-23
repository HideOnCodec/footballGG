package com.footballgg.server.dto.post;

import com.footballgg.server.domain.post.Category;
import com.footballgg.server.domain.post.CategoryConverter;
import com.footballgg.server.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private Long view;
    private Long userId;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname;
    private int favoriteSize;
}
